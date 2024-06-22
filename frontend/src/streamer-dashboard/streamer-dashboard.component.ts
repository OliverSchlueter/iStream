import {Component} from '@angular/core';
import {fetchOnlineStreamers, fetchStream, fetchUser, Stream, User} from "../api/streams";
import {min} from "rxjs";
import {NgIf} from "@angular/common";
import {FormsModule} from "@angular/forms";

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
  title: string = "";
  description: string = "";
  category: string = "";

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

    return hours + ":" + mins + ":" + secs
  }

  public getLiveSinceMS(){
    return Date.now() - this.stream?.liveSince!
  }

  public async safeConfig(){
      const response = await fetch("http://localhost:7457/api/streams/" + this.user?.username, {
        method: "PATCH",
        body: JSON.stringify({
          title: "",
          category: "",
          description: ""
        })
      })

      if (!response.ok) {
        console.error("Error fetching stream")
        return null;
      }

      return response.json();
    }
}
