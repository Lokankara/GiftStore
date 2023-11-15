import {
  Component,
  EventEmitter, Input,
  Output,
  ViewEncapsulation
} from '@angular/core';

@Component({
  selector: 'app-buttons',
  templateUrl: './buttons.component.html',
  styleUrls: ['./buttons.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class ButtonsComponent {

  @Input() prev: string = 'Prev';
  @Input() next: string = 'Next';
  @Input() disable: boolean = false;
  @Output() prevClick = new EventEmitter<void>();
  @Output() nextClick = new EventEmitter<void>();

  sendClick() {
    this.nextClick.emit();
  }

  backClick() {
    this.prevClick.emit();
  }
}
