import { Component } from '@angular/core';
import { AuthService } from './auth.service';
import {NgIf} from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [NgIf],
  template: `
    <button *ngIf="!authService.isAuthenticated" (click)="login()">Login</button>
    <button *ngIf="authService.isAuthenticated" (click)="logout()">Logout</button>
    <p *ngIf="authService.isAuthenticated">Access Token: {{ authService.accessToken }}</p>
  `
})
export class LoginComponent {

  constructor(public authService: AuthService) {}

  login() {
    this.authService.login();
  }

  logout() {
    this.authService.logout();
  }
}
