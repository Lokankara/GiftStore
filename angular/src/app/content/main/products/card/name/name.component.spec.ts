import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NameComponent } from './name.component';

describe('GroupComponent', () => {
  let component: NameComponent;
  let fixture: ComponentFixture<NameComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NameComponent]
    });
    fixture = TestBed.createComponent(NameComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
