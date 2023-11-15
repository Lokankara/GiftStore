import {RouterModule, Routes} from "@angular/router";
import {NgModule} from "@angular/core";
import {LogoutComponent} from "./logout.component";

const routes: Routes = [
  {path: '', component: LogoutComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
})
export class LogoutRoutingModule {
}
