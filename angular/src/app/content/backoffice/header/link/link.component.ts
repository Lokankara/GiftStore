import {Component, Input, OnInit, ViewEncapsulation} from '@angular/core';
import {ILink} from "../../../../interfaces/ILink";
import {LocalStorageService} from "../../../../services/local-storage.service";
import {ICertificate} from "../../../../model/entity/ICertificate";

@Component({
  selector: 'app-user-link',
  templateUrl: './link.component.html',
  styleUrls: ['./link.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class LinkComponent implements OnInit {

  @Input() link!: ILink;
  cardCounter!: number;
  favoriteCounter !: number;

  constructor(
    private readonly storage: LocalStorageService) {
    const certificates: ICertificate[] = this.storage.getCertificates();
    this.cardCounter = certificates
    .filter(cert => cert.checkout)
    .map(cert => cert.count)
    .reduce((acc, value) => acc + value, 0);
    this.favoriteCounter = certificates
    .filter(cert => cert.favorite).length;
  }

  ngOnInit(): void {
    this.storage.cardCounter
    .subscribe((value) =>
      this.cardCounter = value);
    this.storage.favoriteCounter
    .subscribe((value) =>
      this.favoriteCounter = value);
  }
}
