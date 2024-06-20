import {Component} from '@angular/core';
import {fetchOnlineStreamers, fetchUser, User} from "../api/streams";

@Component({
  selector: 'app-streamer-dashboard',
  standalone: true,
  imports: [],
  templateUrl: './streamer-dashboard.component.html',
  styleUrl: './streamer-dashboard.component.css'
})
export class StreamerDashboardComponent {

  user: User | null = null;

  constructor() {
    fetchUser(localStorage.getItem('username')!).then((user) => {
      this.user = user
    })
  }
}
