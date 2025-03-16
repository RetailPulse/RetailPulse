import { Component, OnInit } from '@angular/core';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MenuItem } from 'primeng/api';
import { MatTab, MatTabGroup } from '@angular/material/tabs';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import Fuse from 'fuse.js';
import {Column, FilterOption, InventoryTransaction} from './inventory.model';
import { InventoryService } from './inventory.service';
import {MatFormFieldModule} from '@angular/material/form-field';
import { MatSelectChange, MatSelectModule} from '@angular/material/select';
import { InventoryModalComponent} from "../inventory-modal/inventory-modal.component";

@Component({
  selector: 'app-inventory-management',
  standalone: true,
  imports: [
    TableModule,
    TagModule,
    CommonModule,
    MatTab,
    MatTabGroup,
    MatDialogModule,
    MatFormFieldModule,
    MatSelectModule,
    FormsModule,
  ],
  templateUrl: './inventory-management.component.html',
  styleUrls: ['./inventory-management.component.css'],
})
export class InventoryManagementComponent implements OnInit {
  searchTerm: string = '';
  isMenuOpen: boolean = false;
  isModalOpen: boolean = false;
  cols: Column[] = [];
  limitedCols: Column[] = [];
  filterOptions: FilterOption[] = [];
  selectedFilter: string = '';
  inventoryTransactions: InventoryTransaction[] = [];

  menuItems: MenuItem[] = [
    { label: 'Allocate Product', icon: 'pi pi-plus' },
    { label: 'Import Products', icon: 'pi pi-upload' },
  ];

  // dropdownOptions = [ //fetch buiness entity
  //   { label: 'Option 1', value: 1 },
  //   { label: 'Option 2', value: 2 },
  //   { label: 'Option 3', value: 3 }
  // ];

  //would need to fliter in the second option based on the first option
  selectedOption1: string | null = null;
  selectedOption2: "Warehouse" | "Shop" | "Supplier" | null = null ;

  constructor(private inventoryService: InventoryService, private dialog: MatDialog) {}

  ngOnInit(): void {
    this.loadInventoryTransaction();
    this.initializeColumns();
    this.fetchFilterOptions();
  }

  fetchFilterOptions(): void { //fetch all the shop entity
    this.filterOptions = [
    { "value": "option1", "label": "Option 1" },
    { "value": "option2", "label": "Option 2" },
    { "value": "option3", "label": "Option 3" }
  ]
  }
    // Replace with your API endpoint
    // const apiUrl = 'https://api.example.com/filter-options';
    //
    // this.http.get<FilterOption[]>(apiUrl).subscribe(
    //   (data) => {
    //     this.filterOptions = data; // Assign API response to filterOptions
    //   },
    //   (error) => {
    //     console.error('Error fetching filter options:', error);
    //   }
    // );


  onFilterChange(filterValue: MatSelectChange): void {
    // Implement your filtering logic here
    console.log('Selected Filter:', filterValue);
    // Example: Filter the `filteredProducts` array based on `filterValue`
  }

  private loadInventoryTransaction(): void {
    this.inventoryService.getInventoryTransaction().subscribe((data: InventoryTransaction[]) => {
      this.inventoryTransactions = [... data];
    });
  }

  private initializeColumns(): void {
    this.cols = [
      { field: 'productId', header: 'Product Id' },
      { field: 'quantity', header: 'Quantity' },
      { field: 'costPerUnit', header: 'Cost Per Unit' },
      { field: 'source', header: 'Source' },
      { field: 'destination', header: 'Destination' },

    ];

    this.limitedCols = [
      { field: 'shop', header: 'Shop Name' },
      { field: 'sku', header: 'SKU' },
      { field: 'quantity', header: 'Quantity' },
    ];
  }

  filterProducts(): void {
    const term = this.searchTerm.trim().toLowerCase();
    if (!term) {
      this.inventoryTransactions = [...this.inventoryTransactions];
      return;
    }

    const fuse = new Fuse(this.inventoryTransactions, {
      keys: ['sku', 'shop', 'category'],
      includeScore: true,
      threshold: 0.3,
      ignoreLocation: true,
    });

    this.inventoryTransactions = fuse.search(term).map(result => result.item);
  }

  toggleMenu(event: MouseEvent): void {
    this.isMenuOpen = !this.isMenuOpen;
    event.stopPropagation();
  }

  openModal(index: number): void {
    const dialogRef = this.dialog.open(InventoryModalComponent, {
      data: { title: this.menuItems[index].label,isModalOpen: this.isModalOpen },
      width: '3200px',
      height: '700px',
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        console.log('Selected Options:', result.selectedOption1, result.selectedOption2);
      }
      this.isModalOpen = false;
      this.isMenuOpen = false;
    });

  }
}
