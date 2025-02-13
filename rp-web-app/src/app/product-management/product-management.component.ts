import { Component, OnInit } from '@angular/core';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { CurrencyPipe, CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonDirective } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import Fuse from 'fuse.js';

class Product {
  code!: string;
  name!: string;
  category!: string;
  quantity!: number;
  inventoryStatus!: string;
  price!: number;
}

interface Column {
  field: string;
  header: string;
}

export class ProductService {
  getProductsMini(): Promise<Product[]> {
    const products: Product[] = [
      { code: 'P001', name: 'Product 1', category: 'Category 1', quantity: 10, inventoryStatus: 'INSTOCK', price: 100 },
      { code: 'P002', name: 'Product 2', category: 'Category 2', quantity: 5, inventoryStatus: 'LOWSTOCK', price: 200 },
      { code: 'P003', name: 'Product 3', category: 'Category 3', quantity: 0, inventoryStatus: 'OUTOFSTOCK', price: 300 },
    ];
    return Promise.resolve(products);
  }
}

@Component({
  selector: 'app-product-management',
  standalone: true,
  imports: [
    TableModule,
    TagModule,
    CurrencyPipe,
    CommonModule,
    FormsModule,
    ButtonDirective,
    DialogModule,
  ],
  templateUrl: './product-management.component.html',
  styleUrls: ['./product-management.component.css']
})
export class ProductManagementComponent implements OnInit {
  products!: Product[];
  filteredProducts!: Product[];
  searchTerm: string = '';
  displayModal: boolean = false;
  newProduct: Product = new Product();

  cols!: Column[];

  constructor(private productService: ProductService) {}

  ngOnInit(): void {
    this.productService.getProductsMini().then((data: Product[]) => {
      this.products = data;
      this.filteredProducts = data;
    });

    this.cols = [
      { field: 'code', header: 'SKU' },
      { field: 'name', header: 'Name' },
      { field: 'category', header: 'Category' },
      { field: 'quantity', header: 'Quantity' },
      { field: 'inventoryStatus', header: 'Status' },
      { field: 'price', header: 'Price' }
    ];
  }

  getSeverity(status: string): 'success' | 'info' | 'warn' | 'danger' | 'secondary' | 'contrast' | undefined {
    switch (status) {
      case 'INSTOCK':
        return 'success';
      case 'LOWSTOCK':
        return 'warn';
      case 'OUTOFSTOCK':
        return 'danger';
      default:
        return undefined;
    }
  }

  filterProducts(): void {
    const term = this.searchTerm.trim().toLowerCase();
    if (!term) {
      this.filteredProducts = this.products;
      return;
    }
    const fuse = new Fuse(this.products, {
      keys: ['name', 'code', 'category'],
      includeScore: true,
      threshold: 0.3,
      ignoreLocation: true,
    });

    const results = fuse.search(term);
    this.filteredProducts = results.map(result => result.item);
  }

  createProduct(): void {
    this.displayModal = true;
  }

  saveProduct(): void {
    this.products.push(this.newProduct);
    this.filteredProducts = [...this.products];
    this.newProduct = new Product();
    this.displayModal = false;
  }

  editProduct(product: any) {
    // editing the items
    console.log ('Editing product: ', product);
  }

  deleteProduct(product: any) {
    this.products = this.products.filter(p => p !== product);
    this.filteredProducts = this.filteredProducts.filter(p => p !== product);
  }
}
