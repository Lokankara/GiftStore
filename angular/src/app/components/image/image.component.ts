import {
  AfterViewInit,
  Component, ElementRef, EventEmitter,
  Input, OnInit, Output, ViewChild,
  ViewEncapsulation
} from '@angular/core';

@Component({
  selector: 'app-image',
  templateUrl: './image.component.html',
  styleUrls: ['./image.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class ImageComponent implements OnInit, AfterViewInit {

  @Input() src: string = '';
  @Input() width: number = 0;
  @Input() height: number = 0;
  @Output() action: EventEmitter<void> = new EventEmitter<void>();
  @ViewChild('imageElement', {static: false}) imageElement!: ElementRef;
  private imageCache: { [key: string]: string } = {};

  onImageLoad() {
    if (!this.imageCache[this.src]) {
      const img: HTMLImageElement = new Image();
      img.src = this.src;
      img.onload = () => this.imageCache[this.src] = this.src;
      img.onerror = () => {
        console.error(`Error loading image: ${this.src}`);
      };
    }
    this.action.emit();
  }

  onImageClick() {
    this.action.emit();
  }

  ngOnInit(): void {
    if (!this.src) {
      this.src = '../assets/images/logo2.svg';
    }
    if (this.src.includes('300/')) {
      if (this.width > 300) {
        this.src = this.src.replace('300', String(600))
      } else if (this.width < 300) {
        this.src = this.src.replace('300', String(200))
      }
    }
  }

  ngAfterViewInit(): void {
    if (this.imageElement?.nativeElement.complete) {
      this.onImageLoad();
    }
  }
}
