import {NgModule} from '@angular/core';
import {CategoryComponent} from "./category.component";
import {CardModule} from "../products/card/card.module";
import {NgForOf} from "@angular/common";
import {ImageModule} from "../../../components/image/image.module";
import {ExchangeModule} from "../../../components/exchange/exchange.module";

@NgModule({
  declarations: [
    CategoryComponent
  ],
  exports: [
    CategoryComponent
  ],
  imports: [
    CardModule,
    NgForOf,
    ImageModule,
    ExchangeModule
  ]
})
export class CategoryModule {
}
