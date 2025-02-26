import {ChangeDetectionStrategy, Component, Inject} from '@angular/core';
import {MatDialogRef, MAT_DIALOG_DATA, MatDialogModule} from '@angular/material/dialog';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-inventory-modal',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatFormFieldModule,
    MatSelectModule,
    MatButtonModule,
    MatDialogModule,

  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './inventory-modal.component.html',
  styleUrls: ['./inventory-modal.component.css']
})
export class InventoryModalComponent {
  selectedOption1: number | null = null;
  selectedOption2: number | null = null;

  dropdownOptions = [
    { label: 'Option 1', value: 1 },
    { label: 'Option 2', value: 2 },
    { label: 'Option 3', value: 3 }
  ];

  constructor(
    private dialogRef: MatDialogRef<InventoryModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { title: string, isModalOpen: boolean }
  ) {}

  close(): void {
    this.dialogRef.close();
    this.data.isModalOpen = false;
  }

  submit(): void {
    this.dialogRef.close({
      selectedOption1: this.selectedOption1,
      selectedOption2: this.selectedOption2
    });
  }
}
