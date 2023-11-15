import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {EMPTY} from 'rxjs';
import {map, exhaustMap, catchError} from 'rxjs/operators';
import {CertificateService} from "../../services/certificate.service";
import {
  getCertificatesPending,
  getCertificatesSuccess
} from "../actions/certificate.action";

@Injectable()
export class CertificateEffects {

  getCertificates$ = createEffect(() =>
    this.actions$.pipe(ofType(getCertificatesPending),
      exhaustMap(() => this.certificateService.getCertificates()
      .pipe(map(certificates =>
          getCertificatesSuccess({certificates})),
        catchError(() => EMPTY)
      ))
    )
  );

  constructor(
    private actions$: Actions,
    private certificateService: CertificateService
  ) {
  }
}
