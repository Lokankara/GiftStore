import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {IOrder} from "../../../interfaces/IOrder";
import {IRate} from "../../../interfaces/IRate";
import {ExchangeService} from "../../../components/exchange/exchange.service";
import {OrderService} from "../../../services/order.service";

@Component({
  selector: 'app-booking',
  templateUrl: './bill.component.html',
  styleUrls: ['./bill.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class BillComponent implements OnInit {
  orders!: IOrder[];

  rates!: IRate[];
  index!: number;

  constructor(
    public readonly service: OrderService,
    public readonly exchange: ExchangeService,
  ) {
  }

  ngOnInit() {
    this.service.getOrders().subscribe((data: IOrder[]) => {
      this.orders = data;
      console.log(this.orders)
    });
    this.exchange.currentRates.subscribe(rates => this.rates = rates);
    this.exchange.index$.subscribe(index => this.index = index)
  }
}
