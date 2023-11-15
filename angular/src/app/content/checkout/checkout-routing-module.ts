import {RouterModule, Routes} from "@angular/router";
import {NgModule} from "@angular/core";
import {CheckoutComponent} from "./checkout.component";

const routes: Routes = [
  {path: '', component: CheckoutComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
})
export class CheckoutRoutingModule {
}
