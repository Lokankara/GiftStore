import {Component, ViewEncapsulation} from '@angular/core';
import {SpinnerService} from "./spinner.service";

@Component({
  selector: 'app-spinner',
  templateUrl: './spinner.component.html',
  styleUrls: ['./spinner.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class SpinnerComponent {
  public isVisible = false;

  constructor(public spinner: SpinnerService) {
    this.spinner.getVisibility()
    .subscribe((visible) => {
      this.isVisible = visible;
    });
  }

  protected readonly SpinnerService = SpinnerService;
}
