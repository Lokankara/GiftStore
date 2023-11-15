import {environment} from "../environments/environment";
import {InjectionToken} from "@angular/core";

export const baseUrl: string = environment.baseUrl;
export const BASE_URL_TOKEN: InjectionToken<any> = new InjectionToken('BASE_URL_TOKEN');

export const srcUrl: string = environment.srcUrl;
export const SRC_URL_TOKEN: InjectionToken<any> = new InjectionToken('SRC_URL_TOKEN');

export const nbuUrl: string = environment.nbuUrl;
export const NBU_URL_TOKEN: InjectionToken<any> = new InjectionToken('NBU_URL');
