import {Inject, Injectable} from '@angular/core';
import {
  HttpEvent,
  HttpHandler,
  HttpHeaders,
  HttpInterceptor,
  HttpRequest,
  HttpResponse
} from '@angular/common/http';
import {catchError, EMPTY, filter, map, Observable} from 'rxjs';
import {BASE_URL_TOKEN} from "../../config";
import {SpinnerService} from "../../components/spinner/spinner.service";
import {LoadService} from "../../services/load.service";
import {LoginState} from "../../model/enum/LoginState";

@Injectable()
export class LoadInterceptor implements HttpInterceptor {

  constructor(
    private readonly load: LoadService,
    @Inject(BASE_URL_TOKEN) private baseUrl: string
  ) {
  }

  public intercept(request: HttpRequest<unknown>, next: HttpHandler):
    Observable<HttpEvent<unknown>> {
    SpinnerService.toggle();
    let headers: HttpHeaders = request.headers
    .append('Content-Type', 'application/json')
    .append('Authorization', `Bearer `);
    return next.handle(request.clone({
      url: `${this.baseUrl}${request.url}`,
      headers
    })).pipe(filter(this.isHttpResponse),
      map((response: HttpResponse<any>) => {
        SpinnerService.toggle();
        return response.clone({body: response.body?.data})
      }),
      catchError((error) => {
        this.load.loginState(LoginState.LOGIN_FAILED);
        this.load.modal.showByStatus(error.status);
        SpinnerService.toggle();
        return EMPTY;
      })
    );
  }

  private isHttpResponse(event: HttpEvent<any>):
    event is HttpResponse<any> {
    return event instanceof HttpResponse;
  }
}
