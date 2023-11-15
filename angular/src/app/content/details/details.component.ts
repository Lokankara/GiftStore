import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {ICertificate} from "../../model/entity/ICertificate";
import {CertificateService} from "../../services/certificate.service";
import {Certificate} from "../../model/certificate";

@Component({
  selector: 'app-details',
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class DetailsComponent implements OnInit {
  public itemId!: string;
  certificate: ICertificate;

  constructor(
    private readonly service: CertificateService,
    private activatedRoute: ActivatedRoute) {
    const product = localStorage.getItem('product');
    this.certificate = product ? JSON.parse(product) : new Certificate();
  }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(({id}) => this.itemId = id);
    const certificate: ICertificate = this.service.getById(this.itemId);
    this.certificate.favorite = certificate.favorite;
    this.certificate.checkout = certificate.checkout;
  }
}
