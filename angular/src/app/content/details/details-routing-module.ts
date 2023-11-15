import {RouterModule, Routes} from "@angular/router";
import {NgModule} from "@angular/core";
import {DetailsComponent} from "./details.component";

const routes: Routes = [
  {path: '', component: DetailsComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
})
export class DetailsRoutingModule {
}
