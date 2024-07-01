import {Component} from '@angular/core';
import {fetchStream, fetchStreamConfig, fetchUser, Stream, StreamConfig, User} from "../api/streams";
import {NgIf} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {captureAndSend, receiveData} from "../api/livestream";

@Component({
  selector: 'app-streamer-dashboard',
  standalone: true,
  imports: [
    NgIf,
    FormsModule
  ],
  templateUrl: './streamer-dashboard.component.html',
  styleUrl: './streamer-dashboard.component.css'
})
export class StreamerDashboardComponent {

  user: User | null = null;
  stream: Stream | null = null;
  streamconfig: StreamConfig | null = null;

  constructor() {
    fetchUser(localStorage.getItem('username')!).then((user) => {
      this.user = user
      fetchStreamConfig(this.user?.id!).then((streamconfig) => {
        this.streamconfig = streamconfig
      })
      fetchStream(this.user?.id!).then((stream) => {
        this.stream = stream
      })
    })
  }

  public calculateStreamTime(ms: number) {
    let secs = 0
    let mins = 0
    let hours = 0

    while (ms > 1000) {
      secs++
      ms -= 1000
    }
    while (secs > 60) {
      mins++
      secs -= 60
    }
    while (mins > 60) {
      hours++
      mins -= 60
    }

    return hours + ":" + mins + ":" + secs
  }

  public getLiveSinceMS() {
    return Date.now() - this.stream?.liveSince!
  }

  public async safeConfig() {
    const response = await fetch("http://localhost:7457/api/stream-configs/" + this.user?.id, {
      method: "PATCH",
      body: JSON.stringify({
        title: this.streamconfig?.title,
        category: this.streamconfig?.category,
        description: this.streamconfig?.description
      }),
      headers: {
        username: localStorage.getItem('username')!,
        password: localStorage.getItem('password')!
      }
    })

    if (!response.ok) {
      console.error("Error fetching stream")
      return null;
    }

    return response.json();
  }

  public async startStream() {
    const response = await fetch("http://localhost:7457/api/streams/", {
      method: "POST",
      body: JSON.stringify({
        streamer: this.user?.id
      }),
      headers: {
        username: localStorage.getItem('username')!,
        password: localStorage.getItem('password')!
      }
    })

    if (!response.ok) {
      console.error("Error fetching stream")
    }

    this.startLiveStream()
  }

  public async stopStream() {
    const response = await fetch("http://localhost:7457/api/streams/" + this.user?.id, {
      method: "DELETE",
      body: JSON.stringify({
        streamer: this.user?.id
      }),
      headers: {
        username: localStorage.getItem('username')!,
        password: localStorage.getItem('password')!
      }
    })

    if (!response.ok) {
      console.error("Error fetching stream")
    }

    this.stopLivestream()
  }

  async startLiveStream() {
    await captureAndSend(`ws://localhost:7457/api/streams/${this.user?.id}/live?username=${this.user?.username}&password=${this.user?.password}`);
    await receiveData(document.getElementById('stream') as HTMLVideoElement, `ws://localhost:7457/api/streams/${this.user?.id}/live?username=${this.user?.username}&password=${this.user?.password}`);
  }

  async stopLivestream(){
    location.reload()
  }
}
