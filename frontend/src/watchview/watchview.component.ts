import {Component} from '@angular/core';
import {receiveData} from "../api/livestream";

@Component({
  selector: 'app-watchview',
  standalone: true,
  imports: [],
  templateUrl: './watchview.component.html',
  styleUrl: './watchview.component.css'
})
export class WatchviewComponent {

  streamerId: string = "";

  constructor() {
    window.addEventListener(
      "message",
      (event) => {
        if (event.data.type != "watch") {
          return
        }
        console.log(event.data.streamer)
        this.streamerId = event.data.streamer.streamer
        this.watchLivestream()
      },
      false,
    );
  }

  async watchLivestream() {
    await receiveData(document.getElementById('stream') as HTMLVideoElement, `ws://localhost:7457/api/streams/${this.streamerId}/live`);
    console.log("start")
  }
}
