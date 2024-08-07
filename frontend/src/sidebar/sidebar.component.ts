import {RouterLink} from "@angular/router";
import { Component } from '@angular/core';
import {NgFor, NgForOf} from "@angular/common";
import { Stream, fetchOnlineStreamers, fetchUser } from "../api/streams";


@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [NgForOf, RouterLink],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css'
})
export class SidebarComponent {
  streamer: Stream[] | null = [];

  streamerdummy = [
    {streamer: "Trymacs"},
    {streamer: "Monte"},
    {streamer: "Froids"},
    {streamer: "Drache"},
  ];

  constructor() {
    fetchOnlineStreamers().then((streamers) => {
        this.streamer = streamers;

        for (const s of streamers!) {
          fetchUser(s.streamer).then((user) => {
            if (user) {
              s.streamerName = user.username;
            }
          });
        }
    });
}

  public streamerlink(s: any) {
    window.postMessage({
      type: "watch",
      streamer: s
    })
    console.log("hallo")
  }
}
