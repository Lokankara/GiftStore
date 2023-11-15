import {Component, Input, OnInit, ViewEncapsulation} from '@angular/core';
import {ICertificate} from "../../../../../model/entity/ICertificate";
import {
  LocalStorageService
} from "../../../../../services/local-storage.service";
import {
  ExchangeService
} from "../../../../../components/exchange/exchange.service";
import {IRate} from "../../../../../interfaces/IRate";
import {Store} from "@ngrx/store";
import {IState} from "../../../../../store/reducers";
import {
  addProductToCart,
  removeFromProductToCart
} from "../../../../../store/actions/cart.action";

@Component({
  selector: 'app-price',
  templateUrl: './price.component.html',
  styleUrls: ['./price.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class PriceComponent implements OnInit {
  @Input() certificate: ICertificate = {} as ICertificate;
  rates!: IRate[];
  index!: number;

  constructor(
    private readonly store: Store<IState>,
    public readonly exchange: ExchangeService,
    private readonly storageService: LocalStorageService) {
  }

  ngOnInit() {
    this.exchange.currentRates.subscribe(rates => this.rates = rates);
    this.exchange.index$.subscribe(index => this.index = index)
  }

  public async addToCart(): Promise<void> {
    this.certificate.checkout = !this.certificate.checkout;
    if (this.certificate.checkout){
      this.store.dispatch(addProductToCart({
        product: {
          ...this.certificate,
          count: 1
        }
      }))
    } else {
      this.store.dispatch(removeFromProductToCart({
        id: this.certificate.id
      }))
    }
    this.storageService.updateCertificate(this.certificate);
  }
}
