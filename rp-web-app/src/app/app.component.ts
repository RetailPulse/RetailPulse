import { Component } from '@angular/core';
import {NavigationEnd, Router} from '@angular/router';
// import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  standalone: false
})

export class AppComponent {
  title = 'Retail Pulse';

  isLoginPage = false

  constructor(private router: Router) { //private authService: AuthService,
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.isLoginPage = event.url === '/';
      }
    });
  }

  // login(): void {
  //   this.authService.login();
  // }

  // logout(): void {
  //   this.authService.logout();
  // }
}
