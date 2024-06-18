import { Component } from '@angular/core';
import {RouterLink} from "@angular/router";
import {NgFor, NgForOf} from "@angular/common";


@Component({
  selector: 'app-landingpage',
  standalone: true,
  imports: [NgFor, RouterLink, NgForOf],
  templateUrl: './landingpage.component.html',
  styleUrl: './landingpage.component.css'
})
export class LandingpageComponent {
  StreamerHome = [
    {streamer: "Trymacs", amountViewers:69, streamConfig: {title:"Hallo"}},
    {streamer: "Monte", amountViewers:70, streamConfig: {title:"Hallo"}},
    {streamer: "Drache", amountViewers:71, streamConfig: {title:"Hallo"}},
    {streamer: "Mango", amountViewers:72, streamConfig: {title:"Hallo"}},
    {streamer: "Yavis", amountViewers:73, streamConfig: {title:"Hallo"}},
    {streamer: "IBlali", amountViewers:74, streamConfig: {title:"Hallo"}},
    {streamer: "TryMacs", amountViewers:75, streamConfig: {title:"Hallo"}},
    {streamer: "LLLL", amountViewers:76, streamConfig: {title:"Hallo"}},
    {streamer: "Love", amountViewers:77, streamConfig: {title:"Hallo"}},
    {streamer: "Killer", amountViewers:78, streamConfig: {title:"Hallo"}},
    {streamer: "GubiFortnite", amountViewers:79, streamConfig: {title:"Hallo"}},

      ];
}
