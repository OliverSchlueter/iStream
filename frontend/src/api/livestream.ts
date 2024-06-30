export async function captureAndSend(url: string) {
  const socket = new WebSocket(url);
  socket.onopen = function (event) {
    console.log('WebSocket connection for sending data opened');
  };

  socket.onerror = function (error) {
    console.error('WebSocket error for sending data:', error);
  };

  const stream = await navigator.mediaDevices.getDisplayMedia({
    video: {
      width: {ideal: 1920},
      height: {ideal: 1080},
      frameRate: {ideal: 60},
    },
    audio: true
  });


  const mediaRecorder = new MediaRecorder(stream, {
    mimeType: 'video/webm'
  });

  // uncomment for local video preview
  // localVideo.srcObject = stream;

  mediaRecorder.ondataavailable = function (event) {
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
}

/*
    Receive and display the live stream
 */

let mediaSource: null | MediaSource = null;

export async function receiveData(video: HTMLVideoElement, url: string) {
  const socket = new WebSocket(url);

  socket.onmessage = function (event) {
    console.log('Data received from server');
    if (!mediaSource) {
      mediaSource = new MediaSource();
      video.src = URL.createObjectURL(mediaSource);
      mediaSource.addEventListener('sourceopen', handleSourceOpen);
    }


    appendBlobToSourceBuffer(event.data);
  }

  socket.onopen = function (ev) {
    console.log("connected to websoket")
  }
}

export function handleSourceOpen(event: Event) {
  //mediaSource = this as MediaSource;
  const mimeCodec = 'video/webm; codecs="vp8"'; // Adjust according to your codec
  const sourceBuffer = mediaSource!.addSourceBuffer(mimeCodec);
  sourceBuffer.mode = 'sequence';
}

export function appendBlobToSourceBuffer(blob: Blob) {
  const sourceBuffer = mediaSource!.sourceBuffers[0];
  const reader = new FileReader();
  reader.onload = function (event) {
    sourceBuffer.appendBuffer(event.target!.result as BufferSource);
  };
  reader.readAsArrayBuffer(blob);
}
