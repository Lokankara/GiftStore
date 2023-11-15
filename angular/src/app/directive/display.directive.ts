import {Directive, Input} from '@angular/core';

@Directive({
  selector: '[appDisplay]',
  exportAs: 'displayControl'
})
export class DisplayDirective {

  @Input()
  public display: 'none' | 'block' = 'none';

  loadOn() {
    console.log('block')
    this.display = 'block';
  }

  loadOff() {
    this.display = 'none';
  }
}
