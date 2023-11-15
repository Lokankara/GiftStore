import {Component, OnDestroy, OnInit, ViewEncapsulation} from '@angular/core';
import {ICategory} from '../../../interfaces/ICategory';
import {CertificateService} from "../../../services/certificate.service";
import {LocalStorageService} from "../../../services/local-storage.service";
import {Subject, Subscription, takeUntil} from "rxjs";

@Component({
  selector: 'app-category',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class CategoryComponent implements OnInit, OnDestroy {

  public size: number = 5;
  public index: number = 0;
  public ms: number = 5000;
  public tags$: ICategory[] = [];
  public categories: ICategory[] = [];
  subscription!: Subscription;
  unSubscribers$: Subject<any> = new Subject();

  constructor(
    private storage: LocalStorageService,
    private service: CertificateService,
  ) {
  }

  ngOnInit(): void {
    this.categories = this.storage.getTagsFromLocalStorage();
    if (this.categories.length === 0) {
      this.loadTags();
    }
    this.tags$ = this.categories.slice(0, this.size);
  }

  ngOnDestroy(): void {
    this.unSubscribers$.next(null);
    this.unSubscribers$.complete();
  }

  search(name: string) {
    this.service.findByTagName(name);
  }

  public next(): void {
    this.index = this.index > this.categories.length - this.size - 1 ? 0 : this.index + 1;
    this.tags$ = this.categories.slice(this.index, this.size + this.index);
  }

  public prev(): void {
    this.tags$.pop();
    this.index = this.index > 0 ? this.index - 1 : this.categories.length - 1;
    this.tags$.unshift(this.categories[this.index]);
  }

  loadTags(): void {
    this.subscription = this.service.load.getTags()
    .pipe(takeUntil(this.unSubscribers$))
    .subscribe({
      next: (tags: any): void => {
        if (Array.isArray(tags)) {
          this.storage.saveTagsToLocalStorage(tags);
          this.categories = this.storage.getTagsFromLocalStorage();
          this.tags$ = this.categories.slice(0, this.size);
        }
      },
      error: (error): void => console.error('Error loading tags:', error),
      complete: (): void => console.log('ITag loading completed.'),
    });
  }
}
