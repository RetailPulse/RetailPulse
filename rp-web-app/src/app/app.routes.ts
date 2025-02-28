import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';
import { LoginPageComponent } from './login-page/login-page.component';

// Lazy-loaded components
const lazyAdminPage = () => import('./admin-page/admin-page.component').then(mod => mod.AdminPageComponent);
const lazyOperatorPage = () => import('./operator-page/operator-page.component').then(mod => mod.OperatorPageComponent);
const lazyProductManagement = () => import('./product-management/product-management.component').then(mod => mod.ProductManagementComponent);
const lazyUserManagement = () => import('./user-management/user-management.component').then(mod => mod.UserManagementComponent);
const lazyBusinessEntityManagement = () => import('./business-entity-management/business-entity-management.component').then(mod => mod.BusinessEntityManagementComponent);

export const routes: Routes = [
  // Login route
  { path: '', component: LoginPageComponent },
  { path: 'login', component: LoginPageComponent },

  // Admin routes with guard and role-based access
  {
    path: 'admin',
    loadComponent: lazyAdminPage,
    canActivate: [authGuard],
    data: { roles: ['ADMIN'] },
    children: [
      { path: 'product-management', loadComponent: lazyProductManagement },
      { path: 'user-management', loadComponent: lazyUserManagement },
      { path: 'business-entity-management', loadComponent: lazyBusinessEntityManagement },
      { path: '', redirectTo: 'business-entity-management', pathMatch: 'full' }, // Default child route
    ],
  },

  // Operator route with guard and role-based access
  {
    path: 'operator',
    loadComponent: lazyOperatorPage,
    canActivate: [authGuard],
    data: { roles: ['CASHER', 'MANAGER'] },
  },

  // Default route redirects to login
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  // Wildcard route redirects to login
  { path: '**', redirectTo: '/login' },
];