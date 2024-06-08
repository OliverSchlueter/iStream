import {Component, input} from '@angular/core';
import {FormsModule} from "@angular/forms";

@Component({
  selector: 'app-registration',
  standalone: true,
  imports: [
    FormsModule
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
    const response = await fetch("http://localhost:8080/users", {
      method: "POST",
      body: JSON.stringify({
        username: this.username,
        email: this.mail,
        password: this.password
      })
    })

    if (response.status === 201){
      console.log("User is created")
    } else {
      console.error("Could not create user: "+ response.statusText)
    }
  }
}
