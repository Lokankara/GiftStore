import {Component, ViewEncapsulation} from '@angular/core';
import {IRate} from "../../interfaces/IRate";

@Component({
  selector: 'app-exchange',
  templateUrl: './exchange.component.html',
  styleUrls: ['./exchange.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ExchangeComponent {

  rates$: IRate[] = [];
  public mode: 'off' | 'on' = 'off';
  public ms: number = 5000;
}
