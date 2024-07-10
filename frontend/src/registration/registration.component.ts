import {Component} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-registration',
  standalone: true,
  imports: [
    FormsModule,
    RouterLink
  ],
  templateUrl: './registration.component.html',
  styleUrl: './registration.component.css'
})
export class RegistrationComponent {
  mail: string = "";
  username: string = "";
  password: string = "";
  confirmPassword: string = "";

  public async register() {
    console.log(this.mail, this.username, this.password, this.confirmPassword)
    const response = await fetch((window as any).iStreamBaseUrl + "/api/users", {
      method: "POST",
      body: JSON.stringify({
        username: this.username,
        email: this.mail,
        password: this.password
      })
    })

    if (response.status === 201) {
      console.log("User is created")
      localStorage.setItem('username', this.username)
      localStorage.setItem('email', this.mail)
      localStorage.setItem('password', this.password)
      window.location.assign("/")
    } else {
      console.error("Could not create user: " + response.statusText)
    }
  }
}
