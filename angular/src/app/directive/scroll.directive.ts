import {Directive, HostBinding, HostListener} from '@angular/core';

@Directive({
  selector: '[appScroll]'
})
export class ScrollDirective {
  private isVisible: boolean = false;

  @HostBinding('style.display')
  public display: 'flex' | 'none' = 'none';

  @HostListener('window:scroll', [])
  onWindowScroll() {
    this.isVisible = window.scrollY > 300;
    this.display = this.isVisible ? 'flex' : 'none';
  }

  @HostListener('click')
  onButtonClick() {
    window.scrollTo({
      top: 0,
      behavior: 'smooth',
    });
  }
}
