import { TestBed } from '@angular/core/testing';

import { LoadInterceptor } from '../../shared/service/load.interceptor';

describe('TokenInterceptor', () => {
  beforeEach(() => TestBed.configureTestingModule({
    providers: [
      LoadInterceptor
      ]
  }));

  it('should be created', () => {
    const interceptor: LoadInterceptor = TestBed.inject(LoadInterceptor);
    expect(interceptor).toBeTruthy();
  });
});
