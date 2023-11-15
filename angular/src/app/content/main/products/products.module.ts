import {NgModule} from '@angular/core';
import {CardModule} from "./card/card.module";
import {ProductsComponent} from "./products.component";
import {AsyncPipe, NgForOf, NgIf} from "@angular/common";
import {
  CdkFixedSizeVirtualScroll, CdkVirtualForOf,
  CdkVirtualScrollViewport
} from "@angular/cdk/scrolling";

@NgModule({
  declarations: [
    ProductsComponent,
  ],
  imports: [
    CardModule,
    NgForOf,
    NgIf,
    AsyncPipe,
    CdkFixedSizeVirtualScroll,
    CdkVirtualScrollViewport,
    CdkVirtualForOf
  ],
  exports: [
    ProductsComponent
  ],
})
export class ProductsModule {
}
