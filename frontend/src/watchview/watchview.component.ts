import { Component } from '@angular/core';

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
}
}
