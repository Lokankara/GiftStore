import { NgModule } from '@angular/core';
import {ChevronComponent} from "./chevron/chevron.component";
import {FooterComponent} from "./footer.component";
import {NgClass} from "@angular/common";
import {ScrollDirective} from "../../../directive/scroll.directive";
import {CardModule} from "../../main/products/card/card.module";


@NgModule({
  declarations: [
    ChevronComponent,
    FooterComponent,
    ScrollDirective
  ],
  exports: [
    ChevronComponent,
    FooterComponent,
  ],
  imports: [
    NgClass,
    CardModule
  ]
})
export class FooterModule { }
