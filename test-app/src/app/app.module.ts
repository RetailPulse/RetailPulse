import { NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { BrowserModule } from '@angular/platform-browser';

import { provideHttpClient } from '@angular/common/http'; // Import provideHttpClient
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptor } from './interceptors/auth.interceptor';

@NgModule({
  imports: [
    BrowserModule,
    AppComponent
  ],
  providers: [
    // provideHttpClient(), // Add provideHttpClient to the providers array
    // {
    //   provide: HTTP_INTERCEPTORS,
    //   useClass: AuthInterceptor,
    //   multi: true
    // }
    ],
})
export class AppModule { }
