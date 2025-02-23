import { Component, OnInit } from '@angular/core';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { CurrencyPipe, CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonDirective } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import Fuse from 'fuse.js';
import {Column, Product} from './product.model';
import {InputText} from "primeng/inputtext";
import {InputTextarea} from "primeng/inputtextarea";
import {ProductService} from "./product.service";

@Component({
  selector: 'app-product-management',
  standalone: true,
  imports: [
    TableModule,
    TagModule,
    CurrencyPipe,
    FormsModule,
    ButtonDirective,
    DialogModule,
    InputText,
    CommonModule,
    InputTextarea,
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
  checked: boolean = true;

  constructor(private productService: ProductService) {
  }

  ngOnInit(): void {
    // Use the ProductService to fetch the products from the backend
    this.productService.getProducts().subscribe((data: Product[]) => {
      console.log('Fetched Products:', data);
      // Filter the products to only include those that are active
      this.products = data.filter(product => product.active);
      console.log('Active Products:', this.products);
      this.filteredProducts = this.products;
    });

    this.cols = [
      {field: 'sku', header: 'SKU'},
      {field: 'brand', header: 'Brand'},
      {field: 'category', header: 'Category'},
      {field: 'subcategory', header: 'Subcategory'},
      {field: 'description', header: 'Description'},
      {field: 'rrp', header: 'RRP'},
      {field: 'barcode', header: 'Barcode'},
      {field: 'origin', header: 'Origin'},
      {field: 'uom', header: 'UOM'},
      {field: 'vendor_code', header: 'Vendor Code'}
    ];
  }

  filterProducts(): void {
    const term = this.searchTerm.trim().toLowerCase();
    if (!term) {
      this.filteredProducts = this.products;
      return;
    }
    const fuse = new Fuse(this.products, {
      keys: ['brand', 'sku', 'category', 'subcategory', 'description', 'barcode', 'origin', 'uom', 'vendor_code'],
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
    this.productService.createProduct(this.newProduct).subscribe((createdProduct: Product) => {
      // Add the created product to the list
      this.products.push(createdProduct);
      this.filteredProducts = [...this.products];

      // Reset the new product and close the modal
      this.newProduct = new Product();
      this.displayModal = false;
    }, (error) => {
      console.error('Error creating product:', error);
      // Optionally handle error (e.g., show a toast notification)
    });
  }

  editProduct(product: any) {
    // editing the items
    console.log('Editing product: ', product);
  }


  deleteProduct(product: Product): void {
    this.productService.deleteProduct(product.id).subscribe(() => {
      this.products = this.products.filter(p => p.id !== product.id);
      this.filteredProducts = this.filteredProducts.filter(p => p.id !== product.id);
    }, (error) => {
      console.error('Error deleting product:', error);
    });
  }
}

