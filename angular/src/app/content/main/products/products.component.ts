import {
  AfterViewInit,
  ChangeDetectorRef,
  Component,
  HostListener,
  OnDestroy,
  OnInit,
  ViewEncapsulation
} from '@angular/core';
import {Subject} from "rxjs";
import {ICriteria} from "../../../interfaces/ICriteria";
import {ScrollService} from "../../../services/scroll.service";
import {ICertificate} from "../../../model/entity/ICertificate";
import {CertificateService} from "../../../services/certificate.service";

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.scss'],
  encapsulation: ViewEncapsulation.None,
  // changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ProductsComponent implements OnInit, OnDestroy, AfterViewInit {
  page: number = 0;
  loading: boolean = false;
  unSubscribers$: Subject<any> = new Subject();
  criteria: ICriteria = {name: '', tag: ''};

  constructor(
    private scroll: ScrollService,
    public service: CertificateService,
    private cdr: ChangeDetectorRef
  ) {
  }

  ngAfterViewInit(): void {
    this.cdr.detectChanges();
  }

  ngOnInit(): void {
    const saved: ICertificate[] = this.service.certificates$;
    if (saved.length === 0) {
      this.service.loadMoreCertificates(this.page);
    }
    console.log("Saved certificates in Storage : "
      + this.service.certificates$.length);
  }

  ngOnDestroy(): void {
    this.unSubscribers$.next(null);
    this.unSubscribers$.complete();
    this.scroll.saveScrollPosition();
  }

  @HostListener('window:scroll', ['$event'])
  onScroll(): void {
    if (
      !this.loading &&
      window.innerHeight + window.scrollY >= document.body.offsetHeight - 80
    ) {
      this.service.loadMoreCertificates(this.page);
    }
  }
}
