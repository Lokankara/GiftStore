import {
  Component,
  EventEmitter,
  Input,
  Output,
  ViewEncapsulation
} from '@angular/core';
import {ExchangeService} from "../../../components/exchange/exchange.service";
import {IRate} from "../../../interfaces/IRate";
import {ICertificate} from "../../../model/entity/ICertificate";

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class OrderComponent {

  @Input() coupon!: ICertificate;
  rates!: IRate[];
  index!: number;
  @Output() public remove: EventEmitter<any> = new EventEmitter();
  @Output() public increment: EventEmitter<any> = new EventEmitter();
  @Output() public decrement: EventEmitter<any> = new EventEmitter();

  constructor(
    public readonly exchange: ExchangeService) {
  }

  ngOnInit() {
    this.exchange.currentRates.subscribe((rates: IRate[]) => this.rates = rates);
    this.exchange.index$.subscribe((index: number) => this.index = index)
  }
}
