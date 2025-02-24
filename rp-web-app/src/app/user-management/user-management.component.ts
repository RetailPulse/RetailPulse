import { Component, signal, inject } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import {InputText} from "primeng/inputtext";
import { SelectModule } from 'primeng/select';
import Fuse from 'fuse.js';

import {User, UserRoles} from './user.model';
import {UserService} from './user.service';


@Component({
  selector: 'app-user-management',
  imports: [
    ReactiveFormsModule,
    TableModule,
    TagModule,
    FormsModule,
    InputText,
    ButtonModule,
    DialogModule,
    SelectModule,
    CommonModule
  ],
  templateUrl: './user-management.component.html',
  styleUrl: './user-management.component.css'
})

export class UserManagementComponent {

  private userService = inject(UserService);
  private formBuilder = inject(FormBuilder);
  
  users = signal<User[]>([]);
  filteredUsers = signal<User[]>([]);
  isLoading = signal(true);
  error_msg = signal<string | null>(null);
  success_msg = signal<string | null>(null);


  newUserForm: FormGroup;
  newDialog_visible = signal(false);
  newDialog_error_msg = signal<string | null>(null);
  userRoles = UserRoles;

  constructor() {
    this.userService.getUsers().subscribe({
      next: (data: User[]) => {
        this.users.set(data);
        this.filteredUsers.set(data);
        this.isLoading.set(false);
      },
      error: (err) => {
        this.error_msg.set('Failed to load users. Please try again later.');
        this.isLoading.set(false);
        console.error('Error fetching users:', err);
      }
    });

    this.newUserForm = this.formBuilder.group({
      ctlUsername: ['', Validators.required],
      ctlName: ['', Validators.required],
      ctlEmail: ['', [Validators.required, Validators.email]],
      ctlRole: ['', Validators.required],
    });
  }

  resetMessages(): void {
    this.error_msg.set(null);
    this.success_msg.set(null);
    this.newDialog_error_msg.set(null);
  }

  filterUsers(event: Event): void {
    console.log('Filtering users');
    this.resetMessages();
    
    if (!event.target) {
      return;
    }

    const inputElement = event.target as HTMLInputElement;
    const term = inputElement.value.trim().toLowerCase();

    if (!term || term === '') {
      this.filteredUsers.set(this.users());
      return;
    }
    
    let fuse = new Fuse(this.users(), {
      keys: ['name', 'username', 'email'],
      threshold: 0.3
    })

    const results = fuse.search(term);
    this.filteredUsers.set(results.map(result => result.item));
  }

  isFieldInvalid(fieldName: string): boolean | undefined{
    const control = this.newUserForm.get(fieldName);

    const blnValid = control?.invalid && (control?.touched || control?.dirty);

    if (blnValid === undefined){
      return false;
    }

    return blnValid;      
  }

  showNewUserDialog(): void {
    this.resetMessages();
    console.log('Form State:', this.newUserForm);
    this.newUserForm.reset();
    this.newDialog_visible.set(true);
  }

  registerNewUser(): void {
    this.resetMessages();

    if (this.newUserForm.invalid) {
      // Mark all fields as touched to trigger validation messages
      this.newUserForm.markAllAsTouched();
      this.newDialog_error_msg.set('Please fill in all required fields');
      console.error('Form is invalid:', this.newUserForm.errors);
      return;
    }

    console.log('Casper 0 Username:', this.newUserForm.value.username);

    const newUser = {
      id: 0,
      username: this.newUserForm.value.ctlUsername,
      password: 'password1', // Default password
      email: this.newUserForm.value.ctlEmail,
      name: this.newUserForm.value.ctlName,
      roles: [this.newUserForm.value.ctlRole],
      enabled: true
    };
    
    console.log('Saving new user:', newUser);    
    this.userService.createUser(newUser).subscribe({
      next: (createdUser: User) => {
        console.log('User created:', createdUser);
        this.users.set([...this.users(), createdUser]);
        this.filteredUsers.set([...this.users()]);        
        this.newDialog_visible.set(false);
        this.success_msg.set('User ' + createdUser.username + ' was created successfully');
      },
      error: (err) => {
        this.newDialog_error_msg.set(err);
        console.error(err);
      },
    });
  }

  deleteUser(userId: BigInt): void {
    this.resetMessages();
    console.log(`Delete user with ID: ${userId}`);
  }

  editUser(user: User): void {
    this.resetMessages();
    console.log(`Edit user:`, user);
  }
}
