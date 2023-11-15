import {HostListener, Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class ScrollService {
  @HostListener('window:beforeunload')
  onBeforeUnload() {
   this.saveScrollPosition();
  }

  saveScrollPosition() {
    localStorage.setItem('scrollPosition', (window.scrollY).toString() );
    console.log("saved Position " + window.scrollY.toString())
  }

  restorePosition(): void {
    const savedScrollPosition = localStorage.getItem('scrollPosition');
    if (savedScrollPosition) {
      window.scrollTo(0, parseInt(savedScrollPosition, 10));
    }
    console.log("restore Position " + savedScrollPosition)
  }
}
