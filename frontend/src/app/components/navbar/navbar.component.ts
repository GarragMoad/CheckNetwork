import { Component } from '@angular/core';
import { AuthServiceService } from 'src/app/services/auth-service.service';
import { UserDataServiceService } from 'src/app/services/user/user-data-service.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {
  activeTab = 'network';
  userName: string = '';
  constructor(
    public userDataService : UserDataServiceService
  ){}
  menuItems = [
    { id: 'dashboard', label: 'Dashboard', icon: 'dashboard' },
    { id: 'network', label: 'Network', icon: 'network_wifi' },
    { id: 'devices', label: 'Devices', icon: 'devices' },
    { id: 'settings', label: 'Settings', icon: 'settings' },
    { id: 'logs', label: 'Logs', icon: 'description' }
  ];

  setActiveTab(tabId: string) {
    this.activeTab = tabId;
  }
}
