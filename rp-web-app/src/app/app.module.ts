import { NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { LoginFormComponent } from './login-form/login-form.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppRoutingModule } from './app.routes';
import { BrowserModule } from '@angular/platform-browser';
import { NgbModule, NgbOffcanvasModule } from '@ng-bootstrap/ng-bootstrap';

import { provideHttpClient } from '@angular/common/http'; // Import provideHttpClient
import { OAuthModule } from 'angular-oauth2-oidc';

import {SidebarComponent} from './sidebar/sidebar.component';
import {ProductManagementComponent} from './product-management/product-management.component';
import {ProductService} from './product-management/product-management.component';
import { AuthService } from './services/auth.service';
import { AuthGuardService } from './services/authguard.guard';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    NgbModule,
    NgbOffcanvasModule,
    LoginFormComponent,
    SidebarComponent,
    ProductManagementComponent,
    OAuthModule.forRoot()
  ],
  providers: [
    provideHttpClient(), // Add provideHttpClient to the providers array
    ProductService,   
    AuthService, 
    AuthGuardService
    ],
  bootstrap: [AppComponent]
})
export class AppModule { }
