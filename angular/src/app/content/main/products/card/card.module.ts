import {NgModule} from '@angular/core';
import {CardComponent} from "./card.component";
import {NameComponent} from "./name/name.component";
import {PriceComponent} from "./price/price.component";
import {DescriptionComponent} from "./description/description.component";
import {CarouselDirective} from "../../../../directive/carousel.directive";
import {FilterPipe} from "../../../../pipe/filter.pipe";
import {ImageModule} from "../../../../components/image/image.module";
import {AsyncPipe, CurrencyPipe, NgIf} from "@angular/common";
import {ExchangeModule} from "../../../../components/exchange/exchange.module";
import {RouterLink} from "@angular/router";

@NgModule({
  declarations: [
    CardComponent,
    NameComponent,
    PriceComponent,
    DescriptionComponent,
    CarouselDirective,
    FilterPipe,
  ],
  imports: [
    ImageModule,
    CurrencyPipe,
    ExchangeModule,
    RouterLink,
    AsyncPipe,
    NgIf,
  ],
  exports: [
    FilterPipe,
    CarouselDirective,
    CardComponent
  ],
  providers: [
    FilterPipe,
  ]
})
export class CardModule {
}
