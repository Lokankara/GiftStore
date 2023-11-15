import {Component, OnDestroy, ViewEncapsulation} from '@angular/core';
import {ICertificate} from "../../model/entity/ICertificate";
import {Subscription} from "rxjs";
import {LocalStorageService} from "../../services/local-storage.service";

@Component({
  selector: 'app-favorite',
  templateUrl: './favorite.component.html',
  styleUrls: ['./favorite.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class FavoriteComponent implements OnDestroy {
  certificates$!: ICertificate[];
  private subscription!: Subscription;

  constructor(private storage: LocalStorageService) {
    this.certificates$ = this.storage.getFavoriteCertificates();
    console.log(this.certificates$)
  }

  ngOnDestroy() {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
