import {Component, Input, ViewEncapsulation} from '@angular/core';
import {ExchangeService} from "../../../components/exchange/exchange.service";
import {IRate} from "../../../interfaces/IRate";
import {LoadService} from "../../../services/load.service";

@Component({
  selector: 'app-confirm',
  templateUrl: './confirm.component.html',
  styleUrls: ['./confirm.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ConfirmComponent {
  @Input() total!: number;
  rates!: IRate[];
  index!: number;

  constructor(
    public readonly service: LoadService,
    public readonly exchange: ExchangeService) {
  }

  ngOnInit() {
    this.exchange.currentRates.subscribe(rates => this.rates = rates);
    this.exchange.index$.subscribe(index => this.index = index)
  }
}
