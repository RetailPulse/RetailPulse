import { Component, signal, inject } from '@angular/core';

import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService } from 'primeng/api';

import { User } from '../models/user.model';
import { UserService } from '../services/user.service';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-profile',
  imports: [
    ConfirmDialogModule,
  ],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent {
  private userService = inject(UserService);
  private authService = inject(AuthService);

  myProfile = signal<User| null>(null);

  isLoading = signal(true);
  error_msg = signal<string | null>(null);
  success_msg = signal<string | null>(null);

  constructor() {    
    // Fetch the user profile
    const userName: string = this.authService.getUsername();
    this.userService.getUserByUsername(userName).subscribe({
      next: (data: User) => {
        this.myProfile.set(data);
        this.isLoading.set(false);
      },
      error: (err) => {
        this.error_msg.set('Failed to load users. Please try again later.');
        this.isLoading.set(false);
        console.error('Error fetching users:', err);
      }
    });

  }

  getFirstRole(): string {
    const profile = this.myProfile();

    return profile?.roles?.[0] ?? 'No Role';
  }

  onChangePassword() {
    console.log('Change password clicked');
    // Add logic to handle password change
  }
}
