import { Component } from '@angular/core';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {
  activeTab = 'network';

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
