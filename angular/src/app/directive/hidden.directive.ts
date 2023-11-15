import {Directive, HostBinding} from '@angular/core';

@Directive({
  selector: '[appHidden]',
  exportAs: 'hiddenControl'
})
export class HiddenDirective {

  @HostBinding('style.visibility')
  public visibility: 'visible' | 'hidden' = 'visible';

  public showIcon(): void {
    this.visibility = 'visible';
  }

  public hideIcon(): void {
    this.visibility = 'hidden';
  }
}
