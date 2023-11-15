import {Inject, Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";
import {IRate} from "../../interfaces/IRate";
import {NBU_URL_TOKEN} from "../../config";

@Injectable()
export class ExchangeService {
  private rateSource = new BehaviorSubject<IRate[]>([]);
  private indexSubject: BehaviorSubject<number> = new BehaviorSubject<number>(0);
  public currentRates = this.rateSource.asObservable();

  constructor(  @Inject(NBU_URL_TOKEN) private nbuUrl: string) {
  }

  get index$(): Observable<number> {
    return this.indexSubject.asObservable();
  }

  updateIndex(newIndex: number): void {
    this.indexSubject.next(newIndex);
  }

  changeRates(rates: IRate[]) {
    this.rateSource.next(rates);
  }

  getExchangeRates(): Observable<IRate[]> {
    const selectedCurrencies = ['USD', 'EUR', 'UAH'];

    return new Observable<IRate[]>((observer) => {
      fetch(this.nbuUrl)
      .then((response) => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.json();
      })
      .then((data) => {
        const rates: IRate[] = [];
        let usdRate = 0;
        let euRate = 0;

        data.filter((rate: any) => selectedCurrencies.includes(rate.cc))
        .forEach((rate: any) => {
          if (rate.cc === 'USD') {
            usdRate = rate.rate;
          }
          if (rate.cc === 'EUR') {
            euRate = rate.rate;
          }
        });

        rates.push({
          value: 1.00,
          currency: 'USD',
          src: '../assets/images/us-flag.png'
        });
        rates.push({
          value: usdRate,
          currency: 'UAH',
          src: '../assets/images/ua-flag.png'
        });
        rates.push({
          value: usdRate / euRate,
          currency: 'EUR',
          src: '../assets/images/eu-flag.png'
        });
        observer.next(rates);
        observer.complete();
        this.changeRates(rates);
      })
      .catch((error) => {
        observer.error(error);
      });
    });
  }
}
