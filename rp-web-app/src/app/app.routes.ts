   import { NgModule } from '@angular/core';
   import { RouterModule, Routes } from '@angular/router';
   import { ProductManagementComponent } from './product-management/product-management.component';
   import {LoginFormComponent} from './login-form/login-form.component'; // Import the new component

   export const routes: Routes = [
     { path: '', component: LoginFormComponent },
     { path: 'product-management', component: ProductManagementComponent } // New route for product management
   ];

   @NgModule({
     imports: [RouterModule.forRoot(routes)],
     exports: [RouterModule]
   })
   export class AppRoutingModule { }
