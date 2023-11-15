import {NgModule} from '@angular/core';
import {LogoutComponent} from './logout.component';
import {LogoutRoutingModule} from "./logout-routing-module";


@NgModule({
  declarations: [
    LogoutComponent
  ],
  imports: [
    LogoutRoutingModule
  ]
})
export class LogoutModule {
}
