import {NgModule} from '@angular/core';
import {SpinnerComponent} from "./spinner.component";
import {SpinnerService} from "./spinner.service";
import {NgClass} from "@angular/common";

@NgModule({
  declarations: [
    SpinnerComponent
  ],
  exports: [SpinnerComponent],
  imports: [
    NgClass
  ],
  providers: [
    SpinnerService
  ]
})
export class SpinnerModule {
}
