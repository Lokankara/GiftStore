import {NgModule} from '@angular/core';
import {CardModule} from "../main/products/card/card.module";
import {CategoryModule} from "../main/category/category.module";
import {FavoriteRoutingModule} from "./favorite-routing-module";
import {FavoriteComponent} from "./favorite.component";
import {AsyncPipe, NgForOf, NgIf} from "@angular/common";

@NgModule({
  declarations: [FavoriteComponent],
  imports: [
    CardModule,
    CategoryModule,
    FavoriteRoutingModule,
    NgForOf,
    NgIf,
    AsyncPipe
  ],
  exports: [FavoriteComponent]
})
export class FavoriteModule {
}
