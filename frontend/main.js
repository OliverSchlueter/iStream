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
let recordedChunks = [];

let socket = new WebSocket('ws://localhost:8080/api/streams/1/live');
socket.onopen = function(event) {
    console.log('WebSocket connection for sending data opened');
};

socket.onerror = function(error) {
    console.error('WebSocket error for sending data:', error);
};

async function startCaptureAndSend() {
    try {
        const stream = await navigator.mediaDevices.getDisplayMedia({
            video: true
        });

        mediaRecorder = new MediaRecorder(stream, {
            mimeType: 'video/webm'
        });
        localVideo.srcObject = stream;

        mediaRecorder.ondataavailable = function(event) {
            if (event.data.size > 0) {
                recordedChunks.push(event.data);
            }
        };

        mediaRecorder.start(5000); // Start recording

        // Start sending recorded chunks periodically
        setInterval(sendRecordedChunks, 100); // Adjust interval as needed

    } catch (error) {
        console.error('Error capturing display data:', error);
    }
}

function sendRecordedChunks() {
    if (recordedChunks.length > 0) {
        const blob = new Blob(recordedChunks, {
            type: 'video/webm'
        });
        // localVideo.src = URL.createObjectURL(blob);
        socket.send(blob);
        console.log('Data sent to server:', blob);
        recordedChunks = []; // Clear the recordedChunks array for the next iteration
    }
}

function startReceiveData() {
    console.log('startReceiveData() called');
    socket.onmessage = function(event) {
        const blob = new Blob([event.data], {
            type: 'video/webm'
        });
        const url = URL.createObjectURL(blob);
        localVideo.src = url;
    }
}