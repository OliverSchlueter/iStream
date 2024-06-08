import {RouterLink} from "@angular/router";
import { Component } from '@angular/core';
import {NgFor, NgForOf} from "@angular/common";


@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [NgForOf, RouterLink],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css'
})
export class SidebarComponent {
  streamer = [
    {streamer: "streamer1"},
    {streamer: "streamer2"},
    {streamer: "streamer3"},
    {streamer: "streamer4"},
  ];

  public streamerlink(s: any) {
    window.postMessage({
      type: "watch", 
      streamer: s
    })
    console.log("hallo")
  }
}
