import {Component, ViewEncapsulation} from '@angular/core';

@Component({
  selector: 'app-chevron',
  templateUrl: './chevron.component.html',
  styleUrls: ['./chevron.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class ChevronComponent {
  isVisible!: boolean;
}
