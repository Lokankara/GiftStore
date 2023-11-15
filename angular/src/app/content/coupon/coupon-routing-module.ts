import {RouterModule, Routes} from "@angular/router";
import {NgModule} from "@angular/core";
import {CouponComponent} from "./coupon.component";

const routes: Routes = [
  {path: '', component: CouponComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
})
export class CouponRoutingModule {
}
