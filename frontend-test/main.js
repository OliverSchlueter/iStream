async function startLiveStream() {
    await captureAndSend('ws://localhost:8080/api/streams/oliver/live');
    await receiveData(document.getElementById('remoteVideo'), 'ws://localhost:8080/api/streams/oliver/live');
}

async function watchLiveStream() {
    await receiveData(document.getElementById('remoteVideo'), 'ws://localhost:8080/api/streams/oliver/live');
}