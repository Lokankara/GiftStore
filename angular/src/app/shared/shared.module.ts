import {ModuleWithProviders, NgModule} from '@angular/core';
import {HTTP_INTERCEPTORS} from "@angular/common/http";
import {LoadInterceptor} from "./service/load.interceptor";
import {
  BASE_URL_TOKEN,
  baseUrl,
  NBU_URL_TOKEN, nbuUrl,
  SRC_URL_TOKEN,
  srcUrl
} from "../config";
import {CacheInterceptor} from "./service/cache.interceptor";

@NgModule({
  declarations: [],
  imports: [],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: LoadInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: CacheInterceptor,
      multi: true
    },
  ]
})
export class SharedModule {
  public static forRoot(): ModuleWithProviders<any> {
    return {
      ngModule: SharedModule,
      providers: [
        {
          provide: BASE_URL_TOKEN,
          useValue: baseUrl,
          multi: true
        }, {
          provide: SRC_URL_TOKEN,
          useValue: srcUrl
        }, {
          provide: NBU_URL_TOKEN,
          useValue: nbuUrl
        },
      ]
    }
  }

  public static forChild(): ModuleWithProviders<any> {
    return {
      ngModule:
      SharedModule
    };
  }
}
