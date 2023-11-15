import {
  Component,
  EventEmitter,
  Output,
  ViewEncapsulation
} from '@angular/core';
import {CertificateService} from "../../../../services/certificate.service";

@Component({
  selector: 'app-nav-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class SearchComponent {
  @Output()
  searchQueryChange: EventEmitter<string>;

  constructor(private service: CertificateService) {
    this.searchQueryChange = new EventEmitter<string>()
  }

  onInputChange(text: string): void {
    this.service.criteria.name = text;
    this.service.filter('');
    this.searchQueryChange.emit(text);
  }
}
