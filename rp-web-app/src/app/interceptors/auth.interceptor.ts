import { inject } from '@angular/core';
import {HttpInterceptorFn } from '@angular/common/http';
import { AuthService } from '../services/auth.service';


export const authInterceptor: HttpInterceptorFn = (req, next) => {
  console.log('Processing authInterceptor...');
  // Inject the AuthService
  const authService = inject(AuthService);

  // Get the token from the AuthService
  const token = authService.accessToken;

  // Clone the request and add the authorization header if the token exists
  if (token) {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  console.log('Request after authInterceptor:', req);

  // Pass the request to the next handler
  return next(req);
};