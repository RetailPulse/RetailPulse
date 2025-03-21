import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';

import { BusinessEntityService } from './business-entity.service';

describe('BusinessEntityService', () => { 
  let service: BusinessEntityService;

  beforeEach(() => {  
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
      ],
    });
    service = TestBed.inject(BusinessEntityService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
