import {Component, OnInit} from '@angular/core';
import {AuthService} from '../services/auth.service';
import {MessageModule} from 'primeng/message';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css'], // Corrected from styleUrl to styleUrls
  imports: [MessageModule]
})
export class LoginFormComponent implements OnInit {

  constructor(private authService: AuthService) {
  }

  ngOnInit(): void {
    this.authService.initializeAuth();
  }

  // Method to handle login action
  onLogin(): void {
    // Perform login logic here (e.g., authentication)
    console.log("Logging in...");
    this.authService.login();
  }

}
