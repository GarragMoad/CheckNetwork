import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NodesListComponent } from './nodes-list/nodes-list.component';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { GuacamoleComponent } from './components/guacamole/guacamole.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatDialogModule } from '@angular/material/dialog';
import { NavbarComponent } from './components/navbar/navbar.component';
import { LoginComponent } from './components/login/login.component';
import { NgApexchartsModule } from 'ng-apexcharts';



@NgModule({
  declarations: [
    NodesListComponent,
    AppComponent,
    NodesListComponent,
    GuacamoleComponent,
    NavbarComponent,
    LoginComponent,
    
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    MatIconModule,
    MatListModule,
    HttpClientModule,
    FormsModule,
    MatDialogModule,
    ReactiveFormsModule,
    NgApexchartsModule
  ],
  providers: [ ],
  bootstrap: [AppComponent]
})
export class AppModule { }
