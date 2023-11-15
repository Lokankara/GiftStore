import {Component, Input, ViewEncapsulation} from '@angular/core';
import {ITag} from '../../../../model/entity/ITag';
import {ICertificate} from '../../../../model/entity/ICertificate';
import {CertificateService} from "../../../../services/certificate.service";

@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class CardComponent {
  @Input() tags!: Set<ITag>;
  @Input() certificate!: ICertificate;

  constructor(
    public readonly service: CertificateService
  ) {
  }
}
