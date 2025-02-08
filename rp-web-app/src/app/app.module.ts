import { NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { LoginFormComponent } from './login-form/login-form.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppRoutingModule } from './app.routes';
import { BrowserModule } from '@angular/platform-browser';
import { NgbModule, NgbOffcanvasModule } from '@ng-bootstrap/ng-bootstrap';
import {SidebarComponent} from './sidebar/sidebar.component';
import {ProductManagementComponent} from './product-management/product-management.component';
import {ProductService} from './product-management/product-management.component';


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
    ProductManagementComponent
  ],
  providers: [ProductService],
  bootstrap: [AppComponent]
})
export class AppModule { }
