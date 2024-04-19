// https://github.com/Gerald161/Javascript-Screen-Share-Webrtc/blob/main/Screen%20Share%20Webrtc/main.js

// function start(){
//     navigator.mediaDevices.getDisplayMedia({
//         video: {
//             cursor: "always"
//         },
//         audio: false
//     }).then((stream) => {
//         document.getElementById('localVideo').srcObject = stream;
//         console.log(stream)
//     })
// }

const localVideo = document.getElementById('localVideo');
let mediaRecorder;

let socket = new WebSocket('ws://localhost:8080/api/streams/1/live');
socket.onopen = function(event) {
    console.log('WebSocket connection for sending data opened');
};

socket.onerror = function(error) {
    console.error('WebSocket error for sending data:', error);
};

async function startCaptureAndSend() {
    await receiveData()

    try {
        const stream = await navigator.mediaDevices.getDisplayMedia({
            video: true,
            audio: true
        });

        mediaRecorder = new MediaRecorder(stream, {
            mimeType: 'video/webm'
        });

        // uncomment for local video preview
        // localVideo.srcObject = stream;

        mediaRecorder.ondataavailable = function(event) {
            if (event.data.size > 0) {
                socket.send(event.data);
                console.log('Data sent to server', event.data);
            }
        };

        mediaRecorder.start();

        setInterval(() => {
            mediaRecorder.stop()
            mediaRecorder.start()
        }, 1000);

    } catch (error) {
        console.error('Error capturing display data:', error);
    }
}

let mediaSource = null;
async function receiveData() {
    socket.onmessage = function(event) {
        console.log('Data received from server');
        if (!mediaSource) {
            mediaSource = new MediaSource();
            localVideo.src = URL.createObjectURL(mediaSource);
            mediaSource.addEventListener('sourceopen', handleSourceOpen);
        }
        appendBlobToSourceBuffer(event.data);
    }
}

function handleSourceOpen(event) {
    mediaSource = this;
    const mimeCodec = 'video/webm; codecs="vp8"'; // Adjust according to your codec
    const sourceBuffer = mediaSource.addSourceBuffer(mimeCodec);
    sourceBuffer.mode = 'sequence';
}

function appendBlobToSourceBuffer(blob) {
    const sourceBuffer = mediaSource.sourceBuffers[0];
    const reader = new FileReader();
    reader.onload = function(event) {
        sourceBuffer.appendBuffer(event.target.result);
    };
    reader.readAsArrayBuffer(blob);
}
