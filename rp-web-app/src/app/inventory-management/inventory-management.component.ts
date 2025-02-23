import { Component, OnInit } from '@angular/core';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

import Fuse from 'fuse.js';
import { Column, Product } from './inventory.model';
import { InventoryService } from './inventory.service';
import { MenuItem } from 'primeng/api';
import {MatTab, MatTabGroup} from '@angular/material/tabs';

@Component({
  selector: 'app-inventory-management',
  standalone: true,
  imports: [
    TableModule,
    TagModule,
    FormsModule,
    CommonModule,
    MatTab,
    MatTabGroup,
  ],
  templateUrl: './inventory-management.component.html',
  styleUrls: ['./inventory-management.component.css']
})
export class InventoryManagementComponent implements OnInit {
  products!: Product[];
  filteredProducts!: Product[];
  searchTerm: string = '';
  isMenuOpen: boolean = false;
  cols!: Column[];
  limitedCols!: Column[];

  constructor(private productService: InventoryService) {}

  ngOnInit(): void {
    this.productService.getProducts().subscribe((data: Product[]) => {
      this.products = data.filter(product => product.active);
      this.filteredProducts = this.products;
    });

    this.cols = [
      { field: 'sku', header: 'SKU' },
      { field: 'brand', header: 'Brand' },
      { field: 'category', header: 'Category' },
      { field: 'shop', header: 'Shop Name' },
    ];

    this.limitedCols = [
      {field: "sku", header: "SKU"},
      {field:"shop", header:"Shop Name"},
      {field:"quantity", header:"Quantity"},
    ];
    // Displaying only 5 columns in the second tab
  }

  filterProducts(): void {
    const term = this.searchTerm.trim().toLowerCase();
    if (!term) {
      this.filteredProducts = this.products;
      return;
    }
    const fuse = new Fuse(this.products, {
      keys: ['sku', 'shop', 'category'],
      includeScore: true,
      threshold: 0.3,
      ignoreLocation: true,
    });

    this.filteredProducts = fuse.search(term).map(result => result.item);
  }

  menuItems: MenuItem[] = [
    { label: 'Allocate Product', icon: 'pi pi-plus', command: () => this.allocateProducts() },
    { label: 'Import Products', icon: 'pi pi-upload', command: () => this.importProducts() },
  ];

  toggleMenu(event: MouseEvent) {
    event.stopPropagation();
    this.isMenuOpen = !this.isMenuOpen;
  }

  allocateProducts() {
    console.log('Allocate Product...');
  }

  importProducts() {
    console.log('Importing Products...');
  }
}
