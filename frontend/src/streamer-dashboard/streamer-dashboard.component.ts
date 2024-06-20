import {Component} from '@angular/core';
import {fetchOnlineStreamers, fetchStream, fetchUser, Stream, User} from "../api/streams";
import {min} from "rxjs";

@Component({
  selector: 'app-streamer-dashboard',
  standalone: true,
  imports: [],
  templateUrl: './streamer-dashboard.component.html',
  styleUrl: './streamer-dashboard.component.css'
})
export class StreamerDashboardComponent {

  user: User | null = null;
  stream: Stream | null = null;

  constructor() {
    fetchUser(localStorage.getItem('username')!).then((user) => {
      this.user = user
    })
    fetchStream(localStorage.getItem('username')!).then((stream) => {
      this.stream = stream
    })
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

    return Date.now() -  hours + ":" + mins + ":" + secs
  }
}
