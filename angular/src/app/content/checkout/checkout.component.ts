import {Component, ViewEncapsulation} from '@angular/core';
import {ExchangeService} from "../../components/exchange/exchange.service";
import {ICertificate} from "../../model/entity/ICertificate";
import {Router} from "@angular/router";
import {IRate} from "../../interfaces/IRate";
import {CertificateService} from "../../services/certificate.service";

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class CheckoutComponent {
  rates!: IRate[];
  index!: number;
  checkout$!: ICertificate[];

  constructor(
    private router: Router,
    public readonly exchange: ExchangeService,
    public readonly service: CertificateService,
  ) {
    this.checkout$ = this.service.getCheckoutCertificates();
  }

  ngOnInit() {
    this.exchange.currentRates.subscribe(rates => this.rates = rates);
    this.exchange.index$.subscribe(index => this.index = index)
  }

  get totalAmount(): number {
    return this.checkout$.reduce((total, coupon) => total + coupon.price * coupon.count, 0);
  }

  showDetails(coupon: ICertificate) {
    localStorage.setItem('certificate', JSON.stringify(coupon));
    this.router.navigate([`/product/${coupon.id}/details`])
    .then(() => {
      console.log('Navigation was successful');
    })
    .catch(error => {
      console.error('Navigation failed:', error);
    });
  }

  public increment(product: ICertificate) {
    product.count++;
    this.service.updateCard(product);
  }

  public decrement(product: ICertificate) {
    if (product.count == 1) {
      this.remove(product);
    } else {
      product.count--;
      this.service.updateCard(product);
    }
  }

  public remove(product: ICertificate) {
    product.checkout = false;
    this.service.updateCard(product);
    this.checkout$ = this.service.getCheckoutCertificates();
  }
}
