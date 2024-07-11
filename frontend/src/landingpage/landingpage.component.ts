import {Component} from '@angular/core';
import {RouterLink} from "@angular/router";
import {NgFor, NgForOf} from "@angular/common";
import {fetchOnlineStreamers, fetchUser, Stream} from '../api/streams';


@Component({
  selector: 'app-landingpage',
  standalone: true,
  imports: [NgFor, RouterLink, NgForOf],
  templateUrl: './landingpage.component.html',
  styleUrl: './landingpage.component.css'
})
export class LandingpageComponent {
  StreamerHome: Stream[] | null = [
    {
      streamerName: "Trymacs",
      streamer: "Trymacs",
      amountViewers: 18.574,
      liveSince: 0,
      streamConfig: {title: "Hallo", userId: "", description: "", category: ""}
    },
    {
      streamerName: "ELoTRIX",
      streamer: "ELoTRIX",
      amountViewers: 3.741,
      liveSince: 0,
      streamConfig: {title: "Hallo", userId: "", description: "", category: ""}
    },
    {
      streamerName: "Staiy",
      streamer: "Staiy",
      amountViewers: 6.735,
      liveSince: 0,
      streamConfig: {title: "Hallo", userId: "", description: "", category: ""}
    },
    {
      streamerName: "rewinside",
      streamer: "rewinside",
      amountViewers: 2.516,
      liveSince: 0,
      streamConfig: {title: "Hallo", userId: "", description: "", category: ""}
    },
    {
      streamerName: "ApoRed",
      streamer: "ApoRed",
      amountViewers: 7,
      liveSince: 0,
      streamConfig: {title: "Hallo", userId: "", description: "", category: ""}
    },
    {
      streamerName: "KuchenTV",
      streamer: "KuchenTV",
      amountViewers: 4.236,
      liveSince: 0,
      streamConfig: {title: "Hallo", userId: "", description: "", category: ""}
    },
    {
      streamerName: "GronkhTV",
      streamer: "GronkhTV",
      amountViewers: 6.418,
      liveSince: 0,
      streamConfig: {title: "Hallo", userId: "", description: "", category: ""}
    },
    {
      streamerName: "AnniTheDuck",
      streamer: "AnniTheDuck",
      amountViewers: 3,
      liveSince: 0,
      streamConfig: {title: "Hallo", userId: "", description: "", category: ""}
    },
    {
      streamerName: "HandOfBlood",
      streamer: "HandOfBlood",
      amountViewers: 7.489,
      liveSince: 0,
      streamConfig: {title: "Hallo", userId: "", description: "", category: ""}
    },
    {
      streamerName: "TheRealKnossi",
      streamer: "TheRealKnossi",
      amountViewers: 13.116,
      liveSince: 0,
      streamConfig: {title: "Hallo", userId: "", description: "", category: ""}
    },
    {
      streamerName: "EliasN97",
      streamer: "EliasN97",
      amountViewers: 21.698,
      liveSince: 0,
      streamConfig: {title: "Hallo", userId: "", description: "", category: ""}
    },
    {
      streamerName: "BastiGHG",
      streamer: "BastiGHG",
      amountViewers: 5.842,
      liveSince: 0,
      streamConfig: {title: "Hallo", userId: "", description: "", category: ""}
    },
    {
      streamerName: "ungespielt",
      streamer: "ungespielt",
      amountViewers: 4,
      liveSince: 0,
      streamConfig: {title: "Hallo", userId: "", description: "", category: ""}
    },
    {
      streamerName: "ARD",
      streamer: "ARD",
      amountViewers: 10.542,
      liveSince: 0,
      streamConfig: {title: "Hallo", userId: "", description: "", category: ""}
    },
    {
      streamerName: "Trymacs",
      streamer: "Trymacs",
      amountViewers: 19.124,
      liveSince: 0,
      streamConfig: {title: "Hallo", userId: "", description: "", category: ""}
    },
  ];

  streamer: Stream[] | null = [];

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

      this.streamer!.push(...this.StreamerHome!);
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
