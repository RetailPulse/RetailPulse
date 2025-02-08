import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  standalone: true,
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css'], // Corrected from styleUrl to styleUrls
})
export class LoginFormComponent {
  constructor(private router: Router) {}

  // Method to handle login action
  onLogin(): void {
    // Perform login logic here (e.g., authentication)
    console.log("Login");
    // Navigate to the product management page upon successful login
    this.router.navigate(['product-management']); // No leading slash
  }
}
