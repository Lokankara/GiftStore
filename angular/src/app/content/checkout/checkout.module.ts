import {NgModule} from '@angular/core';
import {OrderComponent} from "./order/order.component";
import {ImageModule} from "../../components/image/image.module";
import {ExchangeModule} from "../../components/exchange/exchange.module";
import {AsyncPipe, CurrencyPipe, NgForOf, NgIf} from "@angular/common";
import {ConfirmComponent} from "./confirm/confirm.component";
import {CheckoutComponent} from "./checkout.component";
import {CheckoutRoutingModule} from "./checkout-routing-module";
import {ButtonsModule} from "../../components/buttons/buttons.module";

@NgModule({
  declarations: [
    CheckoutComponent,
    ConfirmComponent,
    OrderComponent],
  exports: [
    OrderComponent,
    ConfirmComponent,
  ],
  imports: [
    CheckoutRoutingModule,
    ImageModule,
    ExchangeModule,
    CurrencyPipe,
    NgForOf,
    ButtonsModule,
    NgIf,
    AsyncPipe
  ]
})
export class CheckoutModule {
}
