import {RouterModule, Routes} from "@angular/router";
import {NgModule} from "@angular/core";
import {FavoriteComponent} from "./favorite.component";

const routes: Routes = [
  {path: '', component: FavoriteComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
})
export class FavoriteRoutingModule {
}
