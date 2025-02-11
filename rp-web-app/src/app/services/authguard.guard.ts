import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService implements CanActivate {

  constructor(
    private authService: AuthService, 
    private router: Router,    
  ) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    const expectedRoles = route.data['roles'] as Array<string>;
    const userRoles = this.authService.getUserRole();
    console.log('User Roles: ' + userRoles);

    if (this.authService.isAuthenticated && this.hasRequiredRole(userRoles, expectedRoles)) {
      return true;
    } else {
      // Redirect to the login page
      sessionStorage.setItem('errorMessages', 'You are not authorized to access this page.');
      this.router.navigate(['/login']); 
      return false;
    }
  }

  private hasRequiredRole(userRoles: Array<string>, expectedRoles: Array<string>): boolean {
    return expectedRoles.some(role => userRoles.includes(role));
  }
}