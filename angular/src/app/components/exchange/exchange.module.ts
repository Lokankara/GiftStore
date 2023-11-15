import {NgModule} from '@angular/core';
import {ExchangePipe} from "./exchange.pipe";
import {ExchangeComponent} from "./exchange.component";
import {ExchangeDirective} from "./exchange.directive";
import {HiddenDirective} from "../../directive/hidden.directive";
import {AsyncPipe, NgIf, NgOptimizedImage} from "@angular/common";

@NgModule({
  declarations: [
    ExchangePipe,
    ExchangeComponent,
    ExchangeDirective,
    HiddenDirective,
  ],
  exports: [
    ExchangeComponent,
    ExchangePipe,
    HiddenDirective,
  ],
  imports: [
    NgOptimizedImage,
    AsyncPipe,
    NgIf
  ],
  providers: [
    ExchangePipe,
    HiddenDirective,
  ]
})
export class ExchangeModule {
}
