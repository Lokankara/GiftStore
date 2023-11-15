import {Component} from '@angular/core';
import {UserService} from "../../services/user.service";
import {NavigateService} from "../../services/navigate.service";

@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html',
  styleUrls: ['./logout.component.scss']
})
export class LogoutComponent {

  constructor(
    private readonly auth: UserService,
    private readonly navigator: NavigateService
  ) {
    this.auth.logout(auth.getUser());
    this.navigator.redirect("/")
  }
}
