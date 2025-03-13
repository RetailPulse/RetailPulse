import {
  ChangeDetectionStrategy,
  Component,
  Inject,
  OnInit
} from '@angular/core';
import {
  MatDialogRef,
  MAT_DIALOG_DATA, MatDialogContent
} from '@angular/material/dialog';
import { SelectionModel } from '@angular/cdk/collections';
import Fuse from 'fuse.js';
import { Product } from '../product-management/product.model';
import { ProductService } from '../product-management/product.service';
import { BusinessEntity } from '../business-entity-management/business-entity-model';
import { BusinessEntityService } from '../business-entity-management/business-entity.service';
import {MatError, MatFormField, MatLabel} from '@angular/material/form-field';
import {
  MatCell, MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow, MatHeaderRowDef,
  MatRow, MatRowDef,
  MatTable
} from '@angular/material/table';
import {MatCheckbox} from '@angular/material/checkbox';
import {MatOption, MatSelect} from '@angular/material/select';
import {MatIconModule} from '@angular/material/icon';
import {MatInput} from '@angular/material/input';
import {MatButton, MatIconButton} from '@angular/material/button';
import {NgForOf, NgIf} from '@angular/common';
import {
  FormArray,
  FormBuilder,
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';

interface Allocation {
  productId: string;
  productSku: string;
  storeId: number;
  storeName: string;
  quantity: number;
}

@Component({
  selector: 'app-inventory-modal',
  templateUrl: './inventory-modal.component.html',
  styleUrls: ['./inventory-modal.component.css'],
  imports: [
    MatFormField,
    FormsModule,
    MatTable,
    MatCheckbox,
    MatSelect,
    MatOption,
    MatInput,
    MatButton,
    MatHeaderCell,
    MatCell,
    MatColumnDef,
    MatIconButton,
    MatIconModule,
    MatLabel,
    MatError,
    MatHeaderRow,
    MatRow,
    MatDialogContent,
    MatHeaderCellDef,
    NgIf,
    MatCellDef,
    MatRowDef,
    MatHeaderRowDef,
    NgForOf,
    FormsModule,
    ReactiveFormsModule
  ],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class InventoryModalComponent implements OnInit {
  importForm: FormGroup;
  allocationForm: FormGroup;
  products: Product[] = [];
  stores: BusinessEntity[] = [];
  filteredProducts: Product[] = [];
  allocations: Allocation[] = [];
  displayedColumns = ['select', 'sku', 'quantity'];
  displayedColumnsForAllocation = ['product', 'store', 'quantity', 'actions'];
  selection = new SelectionModel<Product>(true, []);
  searchTerm = '';
  productSkus: string[] = [];
  businessEntityNames: string[] = [];
  selectedProductSku: string | null = null;
  selectedBusinessEntityName: string | null = null;

  constructor(
    private dialogRef: MatDialogRef<InventoryModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { title: string, isModalOpen: boolean },
    private productService: ProductService,
    private businessEntityService: BusinessEntityService,
    private fb: FormBuilder
  ) {
    this.importForm = this.fb.group({
      productQuantities: this.fb.array([])
    });

    this.allocationForm = this.fb.group({
      productSku: ['', Validators.required],
      businessEntityName: ['', Validators.required],
      allocationQty: [1, [Validators.required, Validators.min(1)]]
    });
  }

  filterProducts(): void {
    const term = this.searchTerm.trim().toLowerCase();
    if (!term) {
      this.filteredProducts = [...this.products];
    } else {
      const fuse = new Fuse(this.products, {
        keys: ['name', 'sku'],
        threshold: 0.3,
        ignoreLocation: true,
      });
      this.filteredProducts = fuse.search(term).map(result => result.item);
    }
    this.initProductQuantityControls();
  }

  // Controls are added to the productQuantities FormArray in the filterProducts method
  // which is called whenever the product list is filtered:
  initProductQuantityControls(): void {
    const quantitiesFormArray = this.importForm.get('productQuantities') as FormArray;
    quantitiesFormArray.clear();

    this.filteredProducts.forEach(() => {
      const control = new FormControl(1, {
        validators: [Validators.required, Validators.min(1)],
        nonNullable: true // Ensures default value is considered valid
      });

      // Immediately validate and mark as touched
      control.updateValueAndValidity();
      control.markAsTouched();

      quantitiesFormArray.push(control);
    });

    // Trigger parent form validation
    this.importForm.updateValueAndValidity();
  }


  get productQuantities(): FormArray {
    return this.importForm.get('productQuantities') as FormArray;
  }

  getProductQuantityControl(index: number): FormControl {
    if (!this.productQuantities?.controls[index]) {
      return new FormControl(1, [Validators.required, Validators.min(1)]);
    }
    return this.productQuantities.at(index) as FormControl;
  }

  ngOnInit(): void {
    this.importForm = this.fb.group({
      productQuantities: this.fb.array([]) // Initialize empty array first
    });
    this.initializeData();
  }

  private initializeData(): void {
    this.loadProducts();
    this.loadStores();
  }

  loadProducts(): void {
    this.productService.getProducts().subscribe(products => {
    this.products = products.filter(p => p.active);
    this.filteredProducts = [...this.products];
    this.initProductQuantityControls();
    this.productQuantities.updateValueAndValidity(); // Ensure form state update
    });
  }

  loadStores(): void {
    this.businessEntityService.getBusinessEntities().subscribe(stores => {
      this.stores = stores;
      this.businessEntityNames = this.stores.map(store => store.name);
    });
  }

  toggleProduct(product: Product): void {
    this.selection.toggle(product);
    const index = this.filteredProducts.indexOf(product);
    const control = this.getProductQuantityControl(index);
    if (!this.selection.isSelected(product)) {
      control.reset(1);
    }
  }

  toggleAllProducts(): void {
    if (this.allSelected) {
      this.selection.clear();
      this.productQuantities.controls.forEach((control) => {
        control.disable();
        control.reset(1);
      });
    } else {
      this.filteredProducts.forEach((product) => this.selection.select(product));
      this.productQuantities.controls.forEach((control) => control.enable());
    }
  }

  get allSelected(): boolean {
    return this.selection.selected.length === this.filteredProducts.length && this.filteredProducts.length > 0;
  }

  onProductSkuSelected(sku: string): void {
    this.selectedProductSku = sku;
  }

  onBusinessEntitySelected(name: string): void {
    this.selectedBusinessEntityName = name;
  }

  addAllocation(): void {
    if (this.allocationForm.valid && this.selectedProductSku && this.selectedBusinessEntityName) {
      const product = this.products.find(p => p.sku === this.selectedProductSku);
      const store = this.stores.find(s => s.name === this.selectedBusinessEntityName);
      if (product && store) {
        const allocation: Allocation = {
          productId: product.id,
          productSku: product.sku,
          storeId: store.id,
          storeName: store.name,
          quantity: this.allocationForm.get('allocationQty')?.value
        };
        this.allocations.push(allocation);
        this.allocationForm.reset({ allocationQty: 1 });
        this.selectedProductSku = null;
        this.selectedBusinessEntityName = null;
      }
    }
  }

  removeAllocation(index: number): void {
    this.allocations.splice(index, 1);
  }

  submit(): void {
    if (this.data.title === 'Import Products') {
      this.submitImport();
    } else if (this.data.title === 'Allocate Product') {
      this.submitAllocation();
    }
  }

  submitImport(): void {
    if (this.importForm.valid) {
      const importData = this.selection.selected.map((product, index) => ({
        productId: product.id,
        productSku: product.sku,
        quantity: (this.productQuantities.at(index) as FormControl).value,
      }));

      console.log('Import Data:', importData);
      // Submit import data here
    }
    else{
      console.log('Please select at least one product to import');
    }
  }

  submitAllocation(): void {
    console.log('Allocation Data:', this.allocations);
    this.dialogRef.close(this.allocations);
  }

  close(): void {
    this.dialogRef.close();
    this.data.isModalOpen = false;
  }
}
