import {NgModule} from '@angular/core';
import {BillRoutingModule} from "./bill-routing.module";
import {CurrencyPipe, DatePipe, NgForOf, NgIf} from "@angular/common";
import {BillComponent} from "./bill.component";
import {ExchangeModule} from "../../../components/exchange/exchange.module";
import {OrderService} from "../../../services/order.service";

@NgModule({
  declarations: [
    BillComponent,
  ],
  imports: [
    BillRoutingModule,
    NgForOf,
    DatePipe,
    CurrencyPipe,
    ExchangeModule,

    NgIf
  ],
  providers: [
    OrderService]
})
export class BillModule {
}
