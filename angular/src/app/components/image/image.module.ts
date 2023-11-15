import {NgModule} from '@angular/core';
import {ImageComponent} from "./image.component";
import {ImageDirective} from "./image.directive";
import {NgOptimizedImage} from "@angular/common";


@NgModule({
  declarations: [ImageComponent],
  exports: [ImageComponent],
  imports: [
    NgOptimizedImage
  ],
  providers: [ImageDirective]
})
export class ImageModule {
}
