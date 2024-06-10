import { Component } from '@angular/core';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  username: string = "";
  password: string = "";

  public async login(){
    console.log(this.username, this.password)
    const response = await fetch("http://localhost:8080/users", {
      method: "POST",
      body: JSON.stringify({
        username: this.username,
        password: this.password
      })
    })

    if (response.status === 201){
      console.log("You're logged into your account")
    } else {
      console.error("User not found: "+ response.statusText)
    }
  }
}
