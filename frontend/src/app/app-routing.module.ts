import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { NodesListComponent } from './nodes-list/nodes-list.component';
import { AuthGardService } from './services/guard/auth-gard.service';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'test', component: DashboardComponent },
  { 
    path: 'dashboard', 
    component: NodesListComponent,
    canActivate: [AuthGardService]  // Protection de cette route
  },
  { 
    path: '', 
    redirectTo: '/login', 
    pathMatch: 'full' 
  },
  { 
    path: '**', 
    redirectTo: '/login' 
  }
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
