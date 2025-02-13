import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginPageComponent } from './login-page/login-page.component';
import { AdminPageComponent } from './admin-page/admin-page.component';
import { OperatorPageComponent } from './operator-page/operator-page.component';
import { AuthGuardService } from './services/authguard.guard';

export const routes: Routes = [
  { path: '', component: LoginPageComponent },
  { path: 'login', component: LoginPageComponent },
  { path: 'admin', component: AdminPageComponent, canActivate: [AuthGuardService], data: { roles: ['ADMIN', 'SUPER'] } },
  { path: 'operator', component: OperatorPageComponent, canActivate: [AuthGuardService], data: { roles: ['OPERATOR'] }  },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: '**', redirectTo: '/login' }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
