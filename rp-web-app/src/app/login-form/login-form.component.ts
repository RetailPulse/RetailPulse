import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { MessageModule } from 'primeng/message';
import { environment } from '../../environments/environment';
import { BehaviorSubject } from 'rxjs';

@Component({
  standalone: true,
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css'], // Corrected from styleUrl to styleUrls
  imports: [MessageModule]
})
export class LoginFormComponent {
  errorMessage$: BehaviorSubject<string> = new BehaviorSubject<string>('');
  authenciationServerStatus: boolean = false;

  constructor(private router: Router, 
    private route: ActivatedRoute, private authService: AuthService) {     
    // Check the status of the authentication server
    this.authService.configureOAuth().then(status => {        
      this.authenciationServerStatus = status;      
      console.log("Authentication Server Status: " + this.authenciationServerStatus);
      console.log("Role: " + this.authService.getUserRole());

      if (!this.authenciationServerStatus) {
        this.errorMessage$.next("Authentication server is not available. Please try again later.<br>");
      }
    }).catch(error => {
      this.errorMessage$.next("Authentication server is not available. Please try again later.<br>");
    }).then(() => {
              
      // Check if there is an error message in the session state    
      if (sessionStorage.getItem('errorMessages')) {
        this.errorMessage$.next(this.errorMessage$.value + sessionStorage.getItem('errorMessages')?.toString() + "<br>" || "");
        sessionStorage.removeItem('errorMessages');
      }

      if (this.authenciationServerStatus ) {
        if (this.authService.isAuthenticated) {
          // Navigate to the admin or operator page upon successful login
          console.log("Token: \r\n" + this.authService.accessToken);
              
          let userRoles = this.authService.getUserRole();
          if (userRoles.includes("ADMIN") || userRoles.includes("SUPER")) {
            this.router.navigate(['/admin']); 
          }
          else if (userRoles.includes("OPERATOR")) {
            this.router.navigate(['/operator']);
          }
        }
      }  
    });
  }
    
  // Method to handle login action
  onLogin(): void {
    // Perform login logic here (e.g., authentication)
    console.log("Logging in...");
    this.authService.login();
  }
}
