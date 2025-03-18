import { Component, OnInit } from '@angular/core';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatTab, MatTabGroup } from '@angular/material/tabs';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import Fuse from 'fuse.js';
import {Column, FilterOption, InventoryTransaction} from './inventory.model';
import { InventoryService } from './inventory.service';
import {MatFormFieldModule} from '@angular/material/form-field';
import { MatSelectChange, MatSelectModule} from '@angular/material/select';
import { InventoryModalComponent} from "../inventory-modal/inventory-modal.component";
import {forkJoin, map, switchMap} from 'rxjs';
import {BusinessEntityService} from '../business-entity-management/business-entity.service';

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
  errorMessage: string = '';  // This will hold the error message to be displayed in the table
  shopMap:{[id:number]:string}={};


  // menuItems: MenuItem[] = [
  //   { label: 'Allocate Product', },
  //   { label: 'Import Products', icon: 'pi pi-upload' },
  // ];

  // dropdownOptions = [ //fetch buiness entity
  //   { label: 'Option 1', value: 1 },
  //   { label: 'Option 2', value: 2 },
  //   { label: 'Option 3', value: 3 }
  // ];

  //would need to fliter in the second option based on the first option
  selectedOption1: string | null = null;
  selectedOption2: "Warehouse" | "Shop" | "Supplier" | null = null ;

  constructor(private businessEntityService : BusinessEntityService,private inventoryService: InventoryService, private dialog: MatDialog) {}

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
  onFilterChange(filterValue: MatSelectChange): void {
    // Implement your filtering logic here
    console.log('Selected Filter:', filterValue);
    // Example: Filter the `filteredProducts` array based on `filterValue`
  }

  private loadInventoryTransaction(): void {
    this.inventoryService.getInventoryTransaction().pipe(
      switchMap((data: any[]) => {
        if (!data || data.length === 0) {
          this.errorMessage = 'No inventory transactions found.';
          return [];
        }

        // Collect all entity ID requests
        const entityRequests = data.flatMap(item => [
          this.businessEntityService.getBusinessEntityById(item.inventoryTransaction.source),
          this.businessEntityService.getBusinessEntityById(item.inventoryTransaction.destination)
        ]);

        // Perform all requests concurrently
        return forkJoin(entityRequests).pipe(
          map((entities) => {
            return data.map((item, index) => ({
              productSku: item.product.sku,
              quantity: item.inventoryTransaction.quantity,
              costPerUnit: item.inventoryTransaction.costPricePerUnit,
              source: entities[index].name,      // Source entity
              destination: entities[index+1].name // Destination entity
            }));
          })
        );
      })
    ).subscribe({
      next: (result) => {
        this.inventoryTransactions = result;
        this.errorMessage = ''; // Clear error message on success
      },
      error: (error) => {
        console.error('Error fetching inventory transactions:', error);
        this.inventoryTransactions = [];
        this.errorMessage = 'An error occurred while fetching inventory transactions.';
      }
    });
  }


  private initializeColumns(): void {
    this.cols = [
      { field: 'productSku', header: 'Product SKU' },
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

  openModal(): void {
    const dialogRef = this.dialog.open(InventoryModalComponent, {
      // data: { title: this.menuItems[index].label,isModalOpen: this.isModalOpen },
      width: '60%',
      height: 'auto',
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
