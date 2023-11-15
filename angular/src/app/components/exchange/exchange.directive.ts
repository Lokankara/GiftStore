import {
  Directive,
  Input,
  OnInit,
  TemplateRef,
  ViewContainerRef
} from '@angular/core';
import {ExchangeService} from "./exchange.service";
import {IRate} from "../../interfaces/IRate";

@Directive({
  selector: '[appExchangeRates]'
})
export class ExchangeDirective implements OnInit {

  @Input('appExchangeRatesFrom')
  public rates!: IRate[];

  @Input('appExchangeRatesInterval')
  public ms: number = 3000;

  @Input()
  private index: number = 0;

  public autoplay: 'off' | 'on' = 'off';
  private intervalId: number = 0;

  @Input('appExchangeRatesAutoplay')
  public set playAuto(mode: 'off' | 'on') {
    if (!mode) {
      return;
    }
    this.autoplay = mode;
  }

  public context: any;

  constructor(
    private readonly tpl: TemplateRef<any>,
    private readonly vcr: ViewContainerRef,
    private readonly service: ExchangeService,
  ) {
  }
  public next(): void {
    this.resetInterval();
    this.index++;
    if (this.index >= this.rates.length) {
      this.index = 0;
    }
    this.service.updateIndex(this.index);
    this.context.$implicit = this.rates[this.index];
  }

  public prev(): void {
    this.resetInterval();
    this.index--;
    if (this.index < 0) {
      this.index = this.rates.length - 1;
    }
    this.service.updateIndex(this.index);
    this.context.$implicit = this.rates[this.index];
  }

  ngOnInit(): void {
    this.service.getExchangeRates().subscribe(rates => {
      this.rates = rates;
      this.context = {
        $implicit: this.rates[this.index],
        controller: {
          next: () => this.next(),
          prev: () => this.prev(),
        }
      }
      this.vcr.createEmbeddedView(this.tpl, this.context);
      this.resetInterval();
    });
  }

  private initInterval(): this {
    this.intervalId = setInterval(() => {
      this.prev();
    }, this.ms);
    return this;
  }

  private resetInterval(): this {
    if (this.autoplay === 'on') {
      this.clearInterval().initInterval();
    }
    return this;
  }

  private clearInterval(): this {
    if (this.intervalId) {
      clearInterval(this.intervalId);
    }
    return this;
  }
}
