import { NgModule } from '@angular/core';
import {SignupRoutingModule} from "./signup-routing-module";
import {SignupComponent} from "./signup.component";
import {ReactiveFormsModule} from "@angular/forms";
import {ButtonsModule} from "../../components/buttons/buttons.module";

@NgModule({
  declarations: [
    SignupComponent
  ],
  imports: [
    SignupRoutingModule,
    ReactiveFormsModule,
    ButtonsModule
  ]
})
export class SignupModule { }
