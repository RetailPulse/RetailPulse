import { Component, signal, inject } from '@angular/core';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { CurrencyPipe, CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import {InputText} from "primeng/inputtext";
import Fuse from 'fuse.js';

import {User} from './user.model';
import {UserService} from './user.service';


@Component({
  selector: 'app-user-management',
  imports: [
    TableModule,
    TagModule,
    FormsModule,
    ButtonModule,
    DialogModule,
    CommonModule,
  ],
  templateUrl: './user-management.component.html',
  styleUrl: './user-management.component.css'
})

export class UserManagementComponent {

  users = signal<User[]>([]);
  isLoading = signal(true);
  error = signal<string | null>(null);

  constructor(private userService: UserService) {
    this.userService.getUsers().subscribe({
      next: (data: User[]) => {
        this.users.set(data);
        this.isLoading.set(false);
      },
      error: (err) => {
        this.error.set('Failed to load users. Please try again later.');
        this.isLoading.set(false);
        console.error('Error fetching users:', err);
      }
    });
  }

  registerUser(): boolean {
    console.log('Registering user');
    return true;
  }

  deleteUser(userId: BigInt): void {
    console.log(`Delete user with ID: ${userId}`);
  }

  editUser(user: User): void {
    console.log(`Edit user:`, user);
  }
}
