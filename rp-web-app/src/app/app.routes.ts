import { Routes } from '@angular/router';
import { LoginPageComponent } from './login-page/login-page.component';
import { AdminPageComponent } from './admin-page/admin-page.component';
import { OperatorPageComponent } from './operator-page/operator-page.component';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', component: LoginPageComponent },
  { path: 'login', component: LoginPageComponent },
  { path: 'admin', component: AdminPageComponent, canActivate: [authGuard], data: { roles: ['ADMIN', 'SUPER'] } },
  { path: 'operator', component: OperatorPageComponent, canActivate: [authGuard], data: { roles: ['OPERATOR'] }  },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: '**', redirectTo: '/login' },
];
