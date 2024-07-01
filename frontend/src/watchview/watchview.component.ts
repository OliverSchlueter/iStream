import {Component} from '@angular/core';
import {receiveData} from "../api/livestream";
import { Stream, StreamConfig, User, fetchStream, fetchStreamConfig, fetchUser } from '../api/streams';

@Component({
  selector: 'app-watchview',
  standalone: true,
  imports: [],
  templateUrl: './watchview.component.html',
  styleUrl: './watchview.component.css'
})
export class WatchviewComponent {

  streamerId: string = "";
  streamConfig: StreamConfig | null = null;
  stream: Stream | null = null;
  user: User | null = null;

  constructor() {
    window.addEventListener(
      "message",
      (event) => {
        if (event.data.type != "watch") {
          return
        }
        console.log(event.data.streamer)
        this.streamerId = event.data.streamer.streamer
        fetchStreamConfig(this.streamerId) .then((sc)=> {
          this.streamConfig = sc
        })
        fetchStream(this.streamerId) .then((sc)=> {
          this.stream = sc
        })
        fetchUser(this.streamerId) .then((sc)=> {
          this.user = sc
        })
        this.watchLivestream()
      },
      false,
    );
  }

  async watchLivestream() {
    await receiveData(document.getElementById('stream') as HTMLVideoElement, `ws://localhost:7457/api/streams/${this.streamerId}/live`);
    console.log("start")
  }

  public calculateStreamTime(ms: number) {
    let secs = 0
    let mins = 0
    let hours = 0

    while (ms > 1000) {
      secs++
      ms -= 100
    }
    while (secs > 60) {
      mins++
      secs -= 60
    }
    while (mins > 60) {
      hours++
      mins -= 100
    }

    return hours + ":" + mins + ":" + secs
  }

  public getLiveSinceMS() {
    return Date.now() - this.stream?.liveSince!
  }
}

