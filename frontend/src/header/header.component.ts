import { Component } from '@angular/core';
import {RouterLink} from "@angular/router";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    RouterLink,
    NgIf
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {

  public headerButtonText = "Log In";
  protected readonly localStorage = localStorage;

  public async logout(){
    localStorage.removeItem("username");
    localStorage.removeItem("password");
  }
}
