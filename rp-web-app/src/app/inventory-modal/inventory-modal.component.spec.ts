import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InventoryModalComponent } from './inventory-modal.component';
import { MatDialogRef } from '@angular/material/dialog';

beforeEach(async () => {
  await TestBed.configureTestingModule({
    declarations: [InventoryModalComponent],
    providers: [
      { provide: MatDialogRef, useValue: {} }  // Mock MatDialogRef
    ]
  }).compileComponents();
});


describe('InventoryModalComponent', () => {
  let component: InventoryModalComponent;
  let fixture: ComponentFixture<InventoryModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InventoryModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InventoryModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
