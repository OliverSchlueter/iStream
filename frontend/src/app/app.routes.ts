import { Routes } from '@angular/router';
import {RegistrationComponent} from "../registration/registration.component";
import {LoginComponent} from "../login/login.component";
import { WatchviewComponent } from '../watchview/watchview.component';
import {LandingpageComponent} from "../landingpage/landingpage.component";
import {StreamerDashboardComponent} from "../streamer-dashboard/streamer-dashboard.component";

export const routes: Routes = [
  {path: "register", component: RegistrationComponent},
  {path: "login", component: LoginComponent},
  {path: "watchview", component: WatchviewComponent},
  {path: "streamerDashboard", component: StreamerDashboardComponent},
  {path: "", component: LandingpageComponent},
];
