export async function captureAndSend(url: string): Promise<void> {
  const socket = new WebSocket(url);
  socket.onopen = function (event: Event) {
    console.log('WebSocket connection for sending data opened');
  };

  socket.onerror = function (error: Event) {
    console.error('WebSocket error for sending data:', error);
  };

  try {
    let stream: MediaStream;
    try {
      stream = await navigator.mediaDevices.getDisplayMedia({
        video: {
          width: {ideal: 1920},
          height: {ideal: 1080},
          frameRate: {ideal: 60},
          // cursor: 'always',
        },
        audio: true,
      });
    } catch (e) {
      stream = await navigator.mediaDevices.getUserMedia({
        video: {
          width: {ideal: 1920},
          height: {ideal: 1080},
          frameRate: {ideal: 60},
          // cursor: 'always',
        },
        audio: true,
      });
    }

    const mediaRecorder = new MediaRecorder(stream, {
      mimeType: 'video/webm; codecs=vp8',
    });

    // Uncomment for local video preview
    // const localVideo = document.createElement('video');
    // localVideo.srcObject = stream;
    // document.body.appendChild(localVideo);

    mediaRecorder.ondataavailable = function (event: BlobEvent) {
      if (event.data.size > 0) {
        socket.send(event.data);
        console.log('Data sent to server', event.data);
      }
    };

    mediaRecorder.start();

    setInterval(() => {
      mediaRecorder.stop();
      mediaRecorder.start();
    }, 1000);

  } catch (error) {
    console.error('Error capturing display data:', error);
  }
}

let mediaSource: MediaSource | null = null;

export async function receiveData(video: HTMLVideoElement, url: string): Promise<void> {
  const socket = new WebSocket(url);

  socket.onmessage = function (event: MessageEvent<Blob>) {
    console.log('Data received from server');
    if (!mediaSource) {
      mediaSource = new MediaSource();
      video.src = URL.createObjectURL(mediaSource);
      mediaSource.addEventListener('sourceopen', handleSourceOpen);
    }

    appendBlobToSourceBuffer(event.data);
  };
}

function handleSourceOpen(this: MediaSource, event: Event): void {
  console.log('MediaSource opened');
  const mimeCodec = 'video/webm; codecs="vp8"'; // Adjust according to your codec
  const sourceBuffer = this.addSourceBuffer(mimeCodec);
  sourceBuffer.mode = 'sequence';
}

function appendBlobToSourceBuffer(blob: Blob): void {
  if (mediaSource && mediaSource.sourceBuffers.length > 0) {
    const sourceBuffer = mediaSource.sourceBuffers[0];
    const reader = new FileReader();
    reader.onload = function (event: ProgressEvent<FileReader>) {
      if (event.target && event.target.result) {
        sourceBuffer.appendBuffer(event.target.result as ArrayBuffer);
      }
    };
    reader.readAsArrayBuffer(blob);
  }
}
