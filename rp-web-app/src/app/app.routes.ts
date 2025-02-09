import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginFormComponent } from './login-form/login-form.component';
import { AdminPageComponent } from './admin-page/admin-page.component';
import { OperatorPageComponent } from './operator-page/operator-page.component';
import { AuthGuardService } from './services/authguard.guard';

export const routes: Routes = [
  { path: '', component: LoginFormComponent },
  { path: 'login', component: LoginFormComponent },
  { path: 'admin', component: AdminPageComponent, canActivate: [AuthGuardService] },
  { path: 'operator', component: OperatorPageComponent, canActivate: [AuthGuardService] },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: '**', redirectTo: '/login' }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
