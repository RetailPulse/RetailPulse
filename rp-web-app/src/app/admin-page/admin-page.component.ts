import { LogoutButtonComponent } from '../logout-button/logout-button.component';
import { Component, signal } from '@angular/core'; 
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'admin-page',
  imports: [LogoutButtonComponent],
  templateUrl: './admin-page.component.html',
  styleUrl: './admin-page.component.css'
})
export class AdminPageComponent {

  constructor(
    private router: Router, 
    private authService: AuthService,
    private http: HttpClient
  ) { 
    
    if (!this.authService.isAuthenticated) {
      this.router.navigate(['/login']);
    }
  }

  // Method to handle test calling an endpoint from backend.
  onClick(): void {
   //TODO
  }
}