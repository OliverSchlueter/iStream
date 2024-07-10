import {Component} from '@angular/core';
import {FormsModule} from "@angular/forms";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    FormsModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  username: string = "";
  password: string = "";

  public async login() {
    console.log(this.username, this.password)
    const response = await fetch((window as any).iStreamBaseUrl + "/api/validate-user", {
      method: "GET",
      headers: {
        username: this.username,
        password: this.password
      }
    })

    if (response.status === 200) {
      console.log("You're logged into your account")
      localStorage.setItem('username', this.username)
      localStorage.setItem('password', this.password)
      window.location.assign("/")
    } else {
      console.error("Invalid credentials: " + response.statusText)
    }
  }
}
