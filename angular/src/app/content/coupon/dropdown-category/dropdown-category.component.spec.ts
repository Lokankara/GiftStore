import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DropdownCategoryComponent } from './dropdown-category.component';

describe('DropdownCategoryComponent', () => {
  let component: DropdownCategoryComponent;
  let fixture: ComponentFixture<DropdownCategoryComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DropdownCategoryComponent]
    });
    fixture = TestBed.createComponent(DropdownCategoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
