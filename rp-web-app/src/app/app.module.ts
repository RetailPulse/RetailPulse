import { NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { LoginFormComponent } from './login-form/login-form.component';
import  {MatCardModule} from '@angular/material/card';
import {MatButtonModule} from '@angular/material/button';
import {MatInputModule} from '@angular/material/input';
import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {SidebarComponent} from './core/sidebar/sidebar.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginFormComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    MatInputModule,
    MatButtonModule,
    MatCardModule,
    SidebarComponent
  ],
  providers: [],
  bootstrap: [AppComponent,LoginFormComponent]
})
export class AppModule { }
