import { authInterceptor } from "./interceptors/auth.interceptor";

import { OAuthModule } from 'angular-oauth2-oidc';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { ApplicationConfig, provideZoneChangeDetection, importProvidersFrom } from '@angular/core';
import { provideRouter } from '@angular/router'; //, withDebugTracing
import { routes } from './app.routes';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }), 
    provideRouter(routes), //, withDebugTracing()
    provideHttpClient(withInterceptors([authInterceptor])),
    importProvidersFrom(OAuthModule.forRoot())
  ]
};
