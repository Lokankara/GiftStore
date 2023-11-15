import {NgModule} from '@angular/core';
import {BackofficeRoutingModule} from "./backoffice-routing-module";
import {BackofficeComponent} from "./backoffice.component";
import {FooterModule} from "./footer/footer.module";
import {MainModule} from "../main/main.module";
import {LoginModule} from "../login/login.module";
import {SignupModule} from "../signup/signup.module";
import {ReactiveFormsModule} from "@angular/forms";
import {ExchangeModule} from "../../components/exchange/exchange.module";
import {HeaderModule} from "./header/header.module";
import {HttpClientModule} from "@angular/common/http";
import {SpinnerModule} from "../../components/spinner/spinner.module";
import {CertificateService} from "../../services/certificate.service";
import {PreloadService} from "../../shared/service/preload.service";
import {LocalStorageService} from "../../services/local-storage.service";
import {DisplayDirective} from "../../directive/display.directive";
import {FormValidatorDirective} from "../../directive/form-validator.directive";
import {ExchangeService} from "../../components/exchange/exchange.service";
import {BillModule} from "../checkout/bill/bill.module";

@NgModule({
  declarations: [
    BackofficeComponent,
    DisplayDirective,
    FormValidatorDirective
  ],
  imports: [
    BackofficeRoutingModule,
    FooterModule,
    ExchangeModule,
    MainModule,
    LoginModule,
    SignupModule,
    HeaderModule,
    BillModule,
    ReactiveFormsModule,
    HttpClientModule,
    SpinnerModule
  ],
  providers: [
    CertificateService,
    PreloadService,
    LocalStorageService,
    ExchangeService,
  ],
  exports: [
    FormValidatorDirective
  ],
})
export class BackofficeModule {
}
