import { Component } from '@angular/core';
import {RouterLink} from "@angular/router";
import {NgFor, NgForOf} from "@angular/common";
import { Stream, fetchOnlineStreamers } from '../api/streams';


@Component({
  selector: 'app-landingpage',
  standalone: true,
  imports: [NgFor, RouterLink, NgForOf],
  templateUrl: './landingpage.component.html',
  styleUrl: './landingpage.component.css'
})
export class LandingpageComponent {
  StreamerHome: Stream[] | null = [
    {streamer: "Trymacs", amountViewers:69, liveSince:0, streamConfig: {title:"Hallo", userId:"", description:"", category:""}},
    {streamer: "Trymacs", amountViewers:69, liveSince:0, streamConfig: {title:"Hallo", userId:"", description:"", category:""}},
    {streamer: "Trymacs", amountViewers:69, liveSince:0, streamConfig: {title:"Hallo", userId:"", description:"", category:""}},
    {streamer: "Trymacs", amountViewers:69, liveSince:0, streamConfig: {title:"Hallo", userId:"", description:"", category:""}},
    {streamer: "Trymacs", amountViewers:69, liveSince:0, streamConfig: {title:"Hallo", userId:"", description:"", category:""}},
    {streamer: "Trymacs", amountViewers:69, liveSince:0, streamConfig: {title:"Hallo", userId:"", description:"", category:""}},
    {streamer: "Trymacs", amountViewers:69, liveSince:0, streamConfig: {title:"Hallo", userId:"", description:"", category:""}},
    {streamer: "Trymacs", amountViewers:69, liveSince:0, streamConfig: {title:"Hallo", userId:"", description:"", category:""}},
    {streamer: "Trymacs", amountViewers:69, liveSince:0, streamConfig: {title:"Hallo", userId:"", description:"", category:""}},
    {streamer: "Trymacs", amountViewers:69, liveSince:0, streamConfig: {title:"Hallo", userId:"", description:"", category:""}},
    {streamer: "Trymacs", amountViewers:69, liveSince:0, streamConfig: {title:"Hallo", userId:"", description:"", category:""}},
    {streamer: "Trymacs", amountViewers:69, liveSince:0, streamConfig: {title:"Hallo", userId:"", description:"", category:""}},
    {streamer: "Trymacs", amountViewers:69, liveSince:0, streamConfig: {title:"Hallo", userId:"", description:"", category:""}},
    {streamer: "Trymacs", amountViewers:69, liveSince:0, streamConfig: {title:"Hallo", userId:"", description:"", category:""}},
    {streamer: "Trymacs", amountViewers:69, liveSince:0, streamConfig: {title:"Hallo", userId:"", description:"", category:""}},
      ];

      streamer: Stream[] | null = [];
      constructor() {
        fetchOnlineStreamers().then((streamers) => {
            this.streamer = streamers;
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
