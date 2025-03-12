import { Routes } from '@angular/router';
import { LoginPageComponent } from './login-page/login-page.component';
import { AdminPageComponent } from './admin-page/admin-page.component';
import { OperatorPageComponent } from './operator-page/operator-page.component';
import { ProductManagementComponent } from './product-management/product-management.component';
import { UserManagementComponent } from './user-management/user-management.component';
import { BusinessEntityManagementComponent } from './business-entity-management/business-entity-management.component';
import { authGuard } from './guards/auth.guard';
import {ReportGenerationComponent} from './report-generation/report-generation.component';

export const routes: Routes = [
  { path: '', component: LoginPageComponent },
  { path: 'login', component: LoginPageComponent },
  { path: 'admin',
    component: AdminPageComponent,
    canActivate: [authGuard],
    data: { roles: ['ADMIN'] },
    children: [
      { path: 'product-management', component: ProductManagementComponent },
      { path: 'user-management', component: UserManagementComponent },
      { path: 'business-entity-management', component: BusinessEntityManagementComponent},
      { path: 'report-generation', component: ReportGenerationComponent},
      { path: '', redirectTo: 'business-entity-management', pathMatch: 'full' }, // Default route
    ]
  },
  { path: 'operator',
    component: OperatorPageComponent,
    canActivate: [authGuard],
    data: { roles: ['CASHER', 'MANAGER'] }
  },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: '**', redirectTo: '/login' },
];
