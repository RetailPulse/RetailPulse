import { RouterModule } from '@angular/router';
import { Component } from '@angular/core';
import {ProductManagementComponent} from './product-management/product-management.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {CommonModule} from '@angular/common';


@Component({
  selector: 'app-root',
  imports: [RouterModule,ProductManagementComponent],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent {
  title = 'Retail Pulse';
}
