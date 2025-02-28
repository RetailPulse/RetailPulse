import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BusinessEntityManagementComponent } from './business-entity-management.component';

describe('BusinessEntityManagementComponent', () => {
  let component: BusinessEntityManagementComponent;
  let fixture: ComponentFixture<BusinessEntityManagementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BusinessEntityManagementComponent]
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
