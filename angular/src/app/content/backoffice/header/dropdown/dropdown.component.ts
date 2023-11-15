import {
  Component,
  EventEmitter,
  Input,
  Output,
  ViewEncapsulation
} from '@angular/core';
import {CertificateService} from "../../../../services/certificate.service";

@Component({
  selector: 'app-nav-dropdown',
  templateUrl: './dropdown.component.html',
  styleUrls: ['./dropdown.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class DropdownComponent {

  @Input() category: string;
  @Output() dropdownToggle: EventEmitter<void>;
  @Output() categoryClick: EventEmitter<string>;
  @Output() searchInputChange: EventEmitter<string>;

  constructor(private service: CertificateService) {
    this.category = 'All Categories';
    this.dropdownToggle = new EventEmitter<void>()
    this.categoryClick = new EventEmitter<string>();
    this.searchInputChange = new EventEmitter<string>(true);
  }

  onCategoryClick(classList: DOMTokenList, query: string): void {
    const tag = query
      ? query[0].toUpperCase()
      + query.toLowerCase().slice(1)
      : query;
    this.category = (query === '') ? 'All Categories' : query;
    this.categoryClick.emit(query);
    this.service.filter(tag);
    classList.toggle("show");
    this.dropdownToggle.emit();
  }

  toggleDropdown(classList: DOMTokenList): void {
    classList.toggle("show");
    this.dropdownToggle.emit();
  }

  categoryNames: string[] = [
    "Cosmetics",
    "Makeup",
    "Celebration",
    "Travel",
    "Self-care",
    "Culture",
    "Holiday",
    "Anniversary",
  ];
}
