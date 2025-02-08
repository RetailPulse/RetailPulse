import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginFormComponent } from './login-form/login-form.component';
import {ProductManagementComponent} from './product-management/product-management.component';

export const routes: Routes = [
  { path: '', component: LoginFormComponent },
  { path: 'product-management', component: ProductManagementComponent }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
