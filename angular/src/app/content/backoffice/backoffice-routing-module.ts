import {RouterModule, Routes} from "@angular/router";
import {NgModule} from "@angular/core";
import {BackofficeComponent} from "./backoffice.component";
import {AuthGuard} from "../../shared/service/authGuard";
import {CardResolverService} from "../../services/card-resolver.service";

const routes: Routes = [
  {
    path: '', component: BackofficeComponent,
    children: [
      {path: '', loadChildren: () => import('../main/main.module')
        .then(module => module.MainModule)},
      {path: 'coupon', loadChildren: () => import('../coupon/coupon.module')
        .then(module => module.CouponModule)},
      {path: 'details', loadChildren: () => import('../details/details.module')
        .then(module => module.DetailsModule)},
      {path: 'product/:id/details', resolve: {product: CardResolverService},
        loadChildren: () => import('../details/details.module')
        .then(module => module.DetailsModule)},
      {path: 'invoice',  loadChildren: () => import('../checkout/bill/bill.module')
        .then(module => module.BillModule)},
      {path: 'checkout', loadChildren: () => import('../checkout/checkout.module')
        .then(module => module.CheckoutModule), canActivate: [AuthGuard]},
      {path: 'favorite', loadChildren: () => import('../favorite/favorite.module')
        .then(module => module.FavoriteModule), canActivate: [AuthGuard]},
      {path: 'signup', loadChildren: () => import('../signup/signup.module')
        .then(module => module.SignupModule)},
      {path: 'login',  loadChildren: () => import('../login/login.module')
        .then(module => module.LoginModule)},
      {path: 'logout',  loadChildren: () => import('../logout/logout.module')
        .then(module => module.LogoutModule)},
    ]
  }];

@NgModule({
  imports: [
    RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class BackofficeRoutingModule {
}
