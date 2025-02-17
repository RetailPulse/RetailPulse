import { LogoutButtonComponent } from '../logout-button/logout-button.component';
import { Component, Signal } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { HttpClient } from '@angular/common/http';
import { ProductManagementComponent } from '../product-management/product-management.component';

@Component({
  selector: 'admin-page',
  imports: [
    LogoutButtonComponent, 
    ProductManagementComponent
  ],
  templateUrl: './admin-page.component.html',
  styleUrl: './admin-page.component.css',
  standalone: true
})
export class AdminPageComponent {

  // public helloResponse: Signal<string> = (inputStr: string) => {return inputStr;};

  constructor(
    private router: Router,
    private authService: AuthService,
    private http: HttpClient
  ) {

    console.log('Token: ' + this.authService.accessToken);
    if (!this.authService.isAuthenticated) {
      console.log("User is not authenticated. Going back to login page.");
      this.router.navigate(['/login']);
    }
    else {
      console.log("User is authenticated as admin. Staying in admin page.");
    }
  }

  // Method to handle test calling an endpoint from backend.
  onClick(): void {
    this.http.get('http://localhost:9090/hello').subscribe({
      next: (response) => {
        console.log('Response:', response);
      },
      error: (error) => {
        console.error('Error:', error);
      }
    });
  }
}
