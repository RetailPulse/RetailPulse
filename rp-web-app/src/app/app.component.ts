import { Component } from '@angular/core';
import { LoginFormComponent } from './login-form/login-form.component';
import {NavigationEnd, Router} from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  standalone: false
})

export class AppComponent {
  title = 'Retail Pulse';
  isLoginPage = false
  constructor(private router: Router) {
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.isLoginPage = event.url === '/';
      }
    });
  }
}
