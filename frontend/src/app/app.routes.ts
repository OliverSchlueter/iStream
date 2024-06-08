import { Routes } from '@angular/router';
import {RegistrationComponent} from "../registration/registration.component";
import {LoginComponent} from "../login/login.component";
import { WatchviewComponent } from '../watchview/watchview.component';

export const routes: Routes = [
  {path: "register", component: RegistrationComponent},
  {path: "login", component: LoginComponent},
  {path: "watchview", component: WatchviewComponent}
];
