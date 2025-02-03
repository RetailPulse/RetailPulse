import { NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { LoginFormComponent } from './login-form/login-form.component';
import  {MatCardModule} from '@angular/material/card';
import {MatButtonModule} from '@angular/material/button';
import {MatInputModule} from '@angular/material/input';
import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {SidebarComponent} from './core/sidebar/sidebar.component';
import {CoreModule} from './core/core.module';
import {AppRoutingModule} from './app.routes';
import {ProductManagementComponent} from './product-management/product-management.component';

@NgModule({
  declarations: [
    AppComponent

  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    MatInputModule,
    MatButtonModule,
    MatCardModule,
    CoreModule,
    AppRoutingModule,
    LoginFormComponent,
    ProductManagementComponent,
    ProductManagementComponent
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
