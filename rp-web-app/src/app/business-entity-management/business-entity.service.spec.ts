import { TestBed } from '@angular/core/testing';

import { BusinessEntityService } from './business-entity.service';

describe('BusinessEntityService', () => {
  let service: BusinessEntityService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BusinessEntityService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
