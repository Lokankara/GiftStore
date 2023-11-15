import {NgModule} from '@angular/core';
import {LoginRoutingModule} from "./login-routing-module";
import {LoginComponent} from "./login.component";
import {LoginFormComponent} from "./login-form/login-form.component";
import {ReactiveFormsModule} from "@angular/forms";
import {JsonPipe, NgIf} from "@angular/common";

@NgModule({
  declarations: [
    LoginComponent,
    LoginFormComponent],
  imports: [
    LoginRoutingModule,
    ReactiveFormsModule,
    NgIf,
    JsonPipe
  ]
})
export class LoginModule {
}
