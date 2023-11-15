import {
  ActivatedRouteSnapshot,
  CanActivateFn, Router,
  RouterStateSnapshot
} from '@angular/router';
import {inject} from "@angular/core";
import {UserService} from "../../services/user.service";
import {map, take} from "rxjs";
import {LoginState} from "../../model/enum/LoginState";

export const AuthGuard: CanActivateFn = (
  _route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot) => {
  const url: string = state.url;
  return inject(UserService).loginState.pipe(
    take(1),
    map((state: LoginState): boolean => {
      let condition: boolean = url === '/login' || url === '/signup';
      if ((state !== LoginState.LOGGED_IN) && condition) {
        return true;
      }
      if (state === LoginState.LOGGED_IN && condition) {
        inject(Router).createUrlTree(["/backoffice"]);
        return false;
      }
      return state === LoginState.LOGGED_IN;
    })
  );
};
