import { NgModule } from '@angular/core';
import {HeaderComponent} from "./header.component";
import {MenuButtonComponent} from "./menu-button/menu-button.component";
import {UserComponent} from "./user/user.component";
import {LogoComponent} from "./logo/logo.component";
import {SearchComponent} from "./search/search.component";
import {DropdownComponent} from "./dropdown/dropdown.component";
import {LinkComponent} from "./link/link.component";
import {NgForOf, NgIf} from "@angular/common";
import {ExchangeModule} from "../../../components/exchange/exchange.module";
import {RouterLink} from "@angular/router";

@NgModule({
  declarations: [
    HeaderComponent,
    MenuButtonComponent,
    UserComponent,
    LogoComponent,
    SearchComponent,
    DropdownComponent,
    LinkComponent,
  ],
  imports: [
    NgIf,
    NgForOf,
    ExchangeModule,
    RouterLink
  ],
  exports: [
    HeaderComponent
  ],
})
export class HeaderModule { }
