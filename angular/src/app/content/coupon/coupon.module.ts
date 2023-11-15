import {NgModule} from '@angular/core';
import {CouponComponent} from "./coupon.component";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {DropdownCategoryComponent} from "./dropdown-category/dropdown-category.component";
import {CouponRoutingModule} from "./coupon-routing-module";
import {NgClass, NgForOf, NgIf, NgStyle} from "@angular/common";
import {ButtonsModule} from "../../components/buttons/buttons.module";
import { TagsComponent } from './tags/tags.component';

@NgModule({
  declarations: [
    CouponComponent,
    DropdownCategoryComponent,
    TagsComponent
  ],
  imports: [
    ReactiveFormsModule,
    CouponRoutingModule,
    NgForOf,
    NgClass,
    ButtonsModule,
    FormsModule,
    NgIf,
    NgStyle,
  ],
  exports:[
    CouponComponent,
    DropdownCategoryComponent,
    CouponRoutingModule
  ]
})
export class CouponModule { }
