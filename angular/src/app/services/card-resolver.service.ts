import {inject} from '@angular/core';
import {
  ActivatedRouteSnapshot,
  ResolveFn,
  Router,
  RouterStateSnapshot
} from "@angular/router";
import {ICertificate} from "../model/entity/ICertificate";
import {catchError, map, of} from "rxjs";
import {HttpClient} from "@angular/common/http";

export const CardResolverService: ResolveFn<ICertificate | null> = (
  activatedRoute: ActivatedRouteSnapshot,
  _state: RouterStateSnapshot) => {
  const id = activatedRoute.paramMap.get('id') ?? '';
  const router = inject(Router);

  return inject(HttpClient)
  .get<ICertificate>(`/certificates/${id}`)
  .pipe(map((product: ICertificate | null) => {
      if (!product) {
        router.navigate(['/']).then(() => {
          console.log('Navigation has finished');
        });
      }
      localStorage.setItem('product', JSON.stringify(product));
      return product;
    }),
    catchError(() => {
      router.navigate(['/']).then(() => {
        console.log('Navigation has finished');
      });
      return of(null);
    })
  );
};
