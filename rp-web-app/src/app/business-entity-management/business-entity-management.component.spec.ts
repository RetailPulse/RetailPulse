import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AuthService } from '../services/auth.service';
import { createMockAuthService } from '../mock/auth.service.mock';

import { BusinessEntityManagementComponent } from './business-entity-management.component';

describe('BusinessEntityManagementComponent', () => {
  let component: BusinessEntityManagementComponent;
  let fixture: ComponentFixture<BusinessEntityManagementComponent>;

  beforeEach(async () => {
    // Mock AuthService
    mockAuthService = createMockAuthService();

    await TestBed.configureTestingModule({
      imports: [BusinessEntityManagementComponent],
      providers: [
        { provide: AuthService, useValue: mockAuthService }, // Mock AuthService
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BusinessEntityManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
