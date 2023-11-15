import {Component, ViewEncapsulation} from '@angular/core';
import {ILink} from "../../../../interfaces/ILink";
import {UserService} from "../../../../services/user.service";

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class UserComponent {
  constructor(public readonly service: UserService) {
  }

  public userLinks: ILink[] = [
    {
      a: {
        name: "a",
        href: "favorite",
        id: "",
        class: "material-symbols-outlined shop-icon",
        text: "favorite",
      },
      span: {name: "span", id: "favorite-count", class: "counter", text: ""},
    },
    {
      a: {
        name: "a",
        href: "checkout",
        id: "",
        class: "material-symbols-outlined shop-icon",
        text: "shopping_cart",
      },
      span: {name: "span", id: "cart-count", class: "counter", text: ""},
    },
    {
      a: {name: "a", href: "/", id: "splitter", class: "login", text: "|"},
      span: {name: "span", id: "", class: "", text: ""},
    },
    {
      a: {
        name: "a",
        href: this.service.isLoggedIn() ? "logout" : "login",
        id: "login-link",
        class: "login",
        text: this.service.isLoggedIn() ? "Logout" : "Login",
      },
      span: {name: "span", id: "", class: "", text: ""},
    },
    {
      a: {name: "a", href: "/", id: "splitter", class: "login", text: "|"},
      span: {name: "span", id: "", class: "", text: ""},
    },
    {
      a: {
        name: "a",
        href: "signup",
        id: "",
        class: "signup",
        text: "SignUp"
      },
      span: {name: "span", id: "", class: "", text: ""},
    },
  ];
}
