import { ChangeDetectionStrategy, Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogContent } from '@angular/material/dialog';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatCheckbox } from '@angular/material/checkbox';
import { SelectionModel } from '@angular/cdk/collections';
import Fuse from 'fuse.js';
import { Product } from '../product-management/product.model';
import { ProductService } from '../product-management/product.service';
import { BusinessEntity } from '../business-entity-management/business-entity-model';
import { BusinessEntityService } from '../business-entity-management/business-entity.service';

interface Allocation {
  productId: number;
  storeId: number;
  quantity: number;
}

@Component({
  selector: 'app-inventory-modal',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatFormFieldModule,
    MatSelectModule,
    MatButtonModule,
    MatInputModule,
    MatTableModule,
    MatIconModule,
    MatDialogContent,
    MatCheckbox
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './inventory-modal.component.html',
  styleUrls: ['./inventory-modal.component.css']
})
export class InventoryModalComponent implements OnInit {
  // Table Configuration
  displayedColumns = ['select', 'name', 'sku', 'quantity'];
  displayedColumnsForAllocation = ['product', 'store', 'quantity', 'actions'];
  selection = new SelectionModel<Product>(true, []);

  get allSelected(): boolean {
    return !(this.selection.selected.length === this.filteredProducts.length);
  }
  // Data Sources
  products: Product[] = []
  stores: BusinessEntity[] = [];
  filteredProducts: Product[] = [];
  allocations: Allocation[] = [];
  warehouses = [{ id: 1, name: 'Main Warehouse' }];

  // Form Controls
  searchTerm = '';
  selectedSupplier?: number;
  selectedDestination?: number;
  selectedProduct?: number;
  selectedStore?: number;
  allocationQty = 1;

  constructor(
    private dialogRef: MatDialogRef<InventoryModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: {
      title: string;
      isModalOpen: boolean;
      products?: Product[];
      stores?: BusinessEntity[];
    },
    private productService: ProductService,
    private businessEntityService: BusinessEntityService
  ) {}

  ngOnInit(): void {
    this.initializeData();

  }

  private initializeData(): void {
    this.products = this.data.products || [];
    this.stores = this.data.stores || [];

    if (this.products.length === 0) {
      this.fetchProducts();
    } else {
      this.filteredProducts = [...this.products];
    }

    if (this.stores.length === 0) {
      this.fetchStores();
    }
  }

  private fetchProducts(): void {
    this.productService.getProducts().subscribe({
      next: (products) => {
        this.products = products.filter(p => p.active);
        this.filteredProducts = [...this.products];
      },
      error: (error) => console.error('Error fetching products:', error)
    });
  }

  private fetchStores(): void {
    this.businessEntityService.getBusinessEntities().subscribe({
      next: (stores) => this.stores = stores,
      error: (error) => console.error('Error fetching stores:', error)
    });
  }

  filterProducts(): void {
    const term = this.searchTerm.trim().toLowerCase();

    if (!term) {
      this.filteredProducts = [...this.products];
      return;
    }

    const fuse = new Fuse(this.products, {
      keys: ['name', 'sku'],
      threshold: 0.3,
      ignoreLocation: true,
    });

    this.filteredProducts = fuse.search(term).map(result => result.item);
  }

  // Selection Handlers
  toggleProduct(product: Product): void {
    this.selection.toggle(product);
    if (!this.selection.isSelected(product)) {
      console.log(product);
      // put inside the inventory
      // assuming that the product have the quantity to be imported
      // add into the inventory
    }
  }

  toggleAllProducts(): void {
    this.selection.hasValue() && this.selection.selected.length === this.filteredProducts.length
      ? this.selection.clear()
      : this.selection.select(...this.filteredProducts);
  }

  // Allocation Management
  addAllocation(): void {
    if (this.selectedProduct && this.selectedStore && this.allocationQty > 0) {
      this.allocations.push({
        productId: this.selectedProduct,
        storeId: this.selectedStore,
        quantity: this.allocationQty
      });
      this.clearAllocationFields();
    }
  }

  removeAllocation(index: number): void {
    this.allocations.splice(index, 1);
  }

  private clearAllocationFields(): void {
    this.selectedProduct = undefined;
    this.selectedStore = undefined;
    this.allocationQty = 1;
  }

  // Helper Methods
  //getproductSKU

  getProductSKU(productId: string): string {
    return this.products.find(p => p.id === productId)?.sku || 'Unknown';
  }
  getStoreName(storeId: number): string {
    return this.stores.find(s => s.id === storeId)?.name || 'Unknown';
  }

  // Dialog Actions

  submit(): void {
    const importData = {
      supplierId: this.selectedSupplier,
      destinationId: this.selectedDestination,
      products: this.selection.selected.map(p => ({
        productId: p.id,
        productSku: p.sku
      }))
    };
    this.dialogRef.close(importData);
  }

  close(): void {
    this.dialogRef.close();
    this.data.isModalOpen = false;
  }
}
