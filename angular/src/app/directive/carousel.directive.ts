import {
  Directive,
  Input,
  TemplateRef,
  ViewContainerRef,
} from '@angular/core';
import {ICategory} from "../interfaces/ICategory";

@Directive({
  selector: '[appCarousel]',
})
export class CarouselDirective {

  @Input('appCarouselFrom')
  public categories: ICategory[] = [];
  @Input('appCarousel') context: any;

  constructor(
    private templateRef: TemplateRef<any>,
    private containerRef: ViewContainerRef,
  ) {
    this.containerRef.createEmbeddedView(this.templateRef, this.context);
  }
}
