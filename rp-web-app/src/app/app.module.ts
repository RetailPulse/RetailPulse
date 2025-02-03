import { NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { LoginFormComponent } from './login-form/login-form.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {CoreModule} from './core/core.module';
import {AppRoutingModule} from './app.routes';
import {ProductManagementComponent} from './product-management/product-management.component';
import {BrowserModule} from '@angular/platform-browser';
import {NgbModule, NgbOffcanvas, NgbOffcanvasModule} from '@ng-bootstrap/ng-bootstrap';

@NgModule({
  declarations: [
    AppComponent

  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    CoreModule,
    AppRoutingModule,
    LoginFormComponent,
    ProductManagementComponent,
    NgbModule,
    NgbOffcanvasModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
