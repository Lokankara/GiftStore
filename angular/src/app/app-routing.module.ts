import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {BackofficeModule} from "./content/backoffice/backoffice.module";
import {PreloadService} from "./shared/service/preload.service";

const routes: Routes = [
  {path: '', redirectTo: 'backoffice', pathMatch: 'full'},
  {
    path: '',
    loadChildren: () => import('./content/backoffice/backoffice.module')
    .then(module => module.BackofficeModule)
  },
  {path: '**', redirectTo: 'backoffice'},
];

@NgModule({
  imports: [
    BackofficeModule,
    RouterModule.forRoot(routes,
      {preloadingStrategy: PreloadService})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
