import {ModuleWithProviders, NgModule} from '@angular/core';
import {ModalComponent} from "./modal.component";
import {ModalService} from "./modal.service";
import {NgClass} from "@angular/common";
import {SharedModule} from "../../shared/shared.module";

@NgModule({
  declarations: [ModalComponent],
  exports: [ModalComponent],
  imports: [
    NgClass,
    SharedModule.forChild()
  ]
})
export class ModalModule {
  public static forRoot(): ModuleWithProviders<any> {
    return {
      ngModule: ModalModule,
      providers: [
        ModalService
      ]
    };
  }
}
