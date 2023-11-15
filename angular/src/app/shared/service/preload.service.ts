import {Injectable} from '@angular/core';
import {PreloadAllModules, Route} from "@angular/router";
import {delay, mergeMap, Observable, of} from "rxjs";

@Injectable()
export class PreloadService implements PreloadAllModules {
  public preload(route: Route, fn: () => Observable<any>): Observable<any> {
    return of(route).pipe(
      delay(5000),
      mergeMap(() => fn())
    )
  }
}
