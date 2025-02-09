import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { MessageModule } from 'primeng/message';
import { environment } from '../../environments/environment';

@Component({
  standalone: true,
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css'], // Corrected from styleUrl to styleUrls
  imports: [MessageModule]
})
export class LoginFormComponent {
  errorMessage: string = '';
  authenciationServerStatus: boolean = false;

  constructor(private router: Router, private authService: AuthService) {    
    
    if (environment.authEnabled) {
      // // Check the status of the authentication server
      this.authService.configureOAuth().then(status => {
        this.authenciationServerStatus = status;      
        console.log("Authentication Server Status: " + this.authenciationServerStatus);
        console.log("Role: " + this.authService.getUserRole());
      });
      
      if (this.authService.isAuthenticated) {
        // Navigate to the product management page upon successful login
        console.log("Token: \r\n" + this.authService.accessToken);

        let userRoles = this.authService.getUserRole();
        if (userRoles.includes("ADMIN") || userRoles.includes("SUPER")) {
          this.router.navigate(['/product-management']); 
        }
        else if (userRoles.includes("OPERATOR")) {
          this.router.navigate(['/product-management']);
        }
        else {
          this.errorMessage = "You are not authorized to access this site.";
        }

        // this.router.navigate(['product-management']); // No leading slash      
      }
    } else {
      this.authenciationServerStatus = true;
      //TODO: Implement development mode without authentication.
    }    
  }
    
  // Method to handle login action
  onLogin(): void {
    // Perform login logic here (e.g., authentication)
    console.log("Logging in...");
    this.authService.login();
  }
}
