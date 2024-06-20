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
    {streamer: "Trymacs", amountViewers:18.574, liveSince:0, streamConfig: {title:"Hallo", userId:"", description:"", category:""}},
    {streamer: "ELoTRIX", amountViewers:3.741, liveSince:0, streamConfig: {title:"Hallo", userId:"", description:"", category:""}},
    {streamer: "Staiy", amountViewers:6.735, liveSince:0, streamConfig: {title:"Hallo", userId:"", description:"", category:""}},
    {streamer: "rewinside", amountViewers:2.516, liveSince:0, streamConfig: {title:"Hallo", userId:"", description:"", category:""}},
    {streamer: "ApoRed", amountViewers:7, liveSince:0, streamConfig: {title:"Hallo", userId:"", description:"", category:""}},
    {streamer: "KuchenTV", amountViewers:4.236, liveSince:0, streamConfig: {title:"Hallo", userId:"", description:"", category:""}},
    {streamer: "GronkhTV", amountViewers:6.418, liveSince:0, streamConfig: {title:"Hallo", userId:"", description:"", category:""}},
    {streamer: "AnniTheDuck", amountViewers:3, liveSince:0, streamConfig: {title:"Hallo", userId:"", description:"", category:""}},
    {streamer: "HandOfBlood", amountViewers:7.489, liveSince:0, streamConfig: {title:"Hallo", userId:"", description:"", category:""}},
    {streamer: "TheRealKnossi", amountViewers:6911.256, liveSince:0, streamConfig: {title:"Hallo", userId:"", description:"", category:""}},
    {streamer: "EliasN97", amountViewers:21.698, liveSince:0, streamConfig: {title:"Hallo", userId:"", description:"", category:""}},
    {streamer: "BastiGHG", amountViewers:5.842, liveSince:0, streamConfig: {title:"Hallo", userId:"", description:"", category:""}},
    {streamer: "ungespielt", amountViewers:4, liveSince:0, streamConfig: {title:"Hallo", userId:"", description:"", category:""}},
    {streamer: "ARD", amountViewers:10.542, liveSince:0, streamConfig: {title:"Hallo", userId:"", description:"", category:""}},
    {streamer: "Trymacs", amountViewers:19.124, liveSince:0, streamConfig: {title:"Hallo", userId:"", description:"", category:""}},
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
