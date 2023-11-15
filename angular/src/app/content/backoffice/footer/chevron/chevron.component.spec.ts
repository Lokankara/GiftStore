import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChevronComponent } from './chevron.component';

describe('ChevronComponent', () => {
  let component: ChevronComponent;
  let fixture: ComponentFixture<ChevronComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ChevronComponent]
    });
    fixture = TestBed.createComponent(ChevronComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
