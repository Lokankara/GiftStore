import { Component, Input, OnInit, ViewEncapsulation } from '@angular/core';

@Component({
  selector: 'app-description',
  templateUrl: './description.component.html',
  styleUrls: ['./description.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class DescriptionComponent implements OnInit {
  @Input() duration!: number;
  @Input() description!: string;

  ngOnInit(): void {
    this.duration = this.duration > 1 ? this.duration : 1;
  }
}
