import { Routes } from '@angular/router';
import {RegistrationComponent} from "../registration/registration.component";
import {LoginComponent} from "../login/login.component";

export const routes: Routes = [
  {path: "register", component: RegistrationComponent},
  {path: "login", component: LoginComponent},
];
