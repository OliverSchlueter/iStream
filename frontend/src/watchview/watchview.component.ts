import { Component } from '@angular/core';
import {receiveData} from "../api/livestream";

@Component({
  selector: 'app-watchview',
  standalone: true,
  imports: [],
  templateUrl: './watchview.component.html',
  styleUrl: './watchview.component.css'
})
export class WatchviewComponent {
constructor(){
  window.addEventListener(
  "message",
  (event) => {
    if(event.data.type != "watch" ){
      return
    }
    console.log(event.data.streamer)
  },
  false,
);

  this.watchLivestream()
}

async watchLivestream(){
  await receiveData(document.getElementById("stream") as HTMLVideoElement, "ws://localhost:8080/api/streams/" + localStorage.getItem("user") + "/live")
  console.log("start")
}
}
