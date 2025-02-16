import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { provideAnimations } from '@angular/platform-browser/animations';
import { provideRouter } from '@angular/router';
import { routes } from './app/app.routes';
import {importProvidersFrom} from '@angular/core';
import {HttpClient, HttpClientModule} from '@angular/common/http';
import {OAuthModule} from 'angular-oauth2-oidc';

bootstrapApplication(AppComponent, {
  providers: [
    provideRouter(routes),
    provideAnimations(),
    importProvidersFrom(OAuthModule,HttpClientModule)
  ]
}).catch(err => console.error(err));
