import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { LogoutButtonComponent } from '../logout-button/logout-button.component';

@Component({
  selector: 'admin-page',
  imports: [LogoutButtonComponent],
  templateUrl: './admin-page.component.html',
  styleUrl: './admin-page.component.css'
})
export class AdminPageComponent {

}
