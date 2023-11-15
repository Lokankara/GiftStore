import {Component, Input, ViewEncapsulation} from '@angular/core';
import {ICertificate} from "../../../../../model/entity/ICertificate";
import {LocalStorageService} from "../../../../../services/local-storage.service";

@Component({
  selector: 'app-name',
  templateUrl: './name.component.html',
  styleUrls: ['./name.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class NameComponent {

  @Input() certificate: ICertificate = {} as ICertificate;

  constructor(private storageService: LocalStorageService) {
  }

  public addToFavorite() {
    this.certificate.favorite = !this.certificate.favorite;
    this.storageService.updateCertificate(this.certificate);
  }
}
