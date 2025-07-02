import { Component, OnInit } from '@angular/core';
import { WebSocketService } from '../services/web-socket.service';
import { GetDashboardService } from '../services/get-dashboard.service';
import { GuacamoleService } from '../services/guacamole.service';
import { MatDialog } from '@angular/material/dialog';
import { SnmpStatusService } from '../services/snmp-status.service';
import { UserDataServiceService } from '../services/user/user-data-service.service';
import { AuthServiceService } from '../services/auth-service.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

interface Node {
  id?: string;
  ip: string;
  hostname: string;
  status: 'UP' | 'DOWN';
  snmpRunning: boolean;
  grafanaUrl?: string;
}

@Component({
  selector: 'app-nodes-list',
  templateUrl: './nodes-list.component.html',
  styleUrls: ['./nodes-list.component.scss'],
})
export class NodesListComponent implements OnInit {
  snmpStatus: any[] = [];

  nodes: any[] = [];
  filteredNodes: Node[] = [];
  searchTerm: string = '';
  currentFilter: 'all' | 'active' | 'inactive' = 'all';
  viewMode: 'cards' | 'table' = 'cards';

  constructor(
    private webSocketService: WebSocketService,
    private getDashboardService: GetDashboardService,
    private guacamoleService: GuacamoleService,
    private snmpStatusService: SnmpStatusService,
    private userDataService: UserDataServiceService,
    private authService: AuthServiceService
  ) {}

  ngOnInit(): void {
    this.loadSnmpStatus();
    this.userDataService.userName =  this.authService.getUserName() || 'Guest'; // Set default user name if not logged in
    this.webSocketService.nodes$.subscribe(nodes => {
      this.nodes = nodes;
      this.enrichWithGrafanaUrls();
      this.mergeSnmpStatus();
    });
  }

  enrichWithGrafanaUrls(): void {
    this.nodes.forEach(node => {
      const title = `Network-Performance-${node.ip}`;
      this.getDashboardService.getDashboardUrl(title).subscribe({
        next: (url: any) => node.grafanaUrl = url,
        error: (err: any) => {
          console.warn(`No dashboard for ${node.ip}`, err);
          node.grafanaUrl = null;
        }
      });
    });
  }

  refresh(): void {
    this.webSocketService.requestNetworkStatus();
  }

 openConsole(node: any): void {
   if (node.status === 'DOWN') return;
  this.guacamoleService.getConnectionUrlByIp(node.ip).subscribe({
    next: res => {
      // Ouvre l'URL dans un nouvel onglet
      window.open(res.url, '_blank');
    },
    error: err => {
      console.error('Erreur lors de l\'ouverture de la console', err);
    }
  });
}
loadSnmpStatus(): void {
    this.snmpStatusService.getSnmpStatus().subscribe({
      next: (data) => {
        this.snmpStatus = data;
        this.mergeSnmpStatus();
      },
      error: (err) => {
        console.error('Erreur lors du chargement du statut SNMP', err);
      }
    });
  }

  mergeSnmpStatus(): void {
    if (this.nodes.length > 0 && this.snmpStatus.length > 0) {
      this.nodes.forEach(node => {
        const snmpNode = this.snmpStatus.find(s => s.ip === node.ip);
        if (snmpNode) {
          node.snmpRunning = snmpNode.snmpRunning;
        }
      });
    }
  }

   getDeviceType(hostname: string): string {
    const name = hostname.toLowerCase();
    if (name.includes('router')) return 'Router';
    if (name.includes('switch')) return 'Switch';
    if (name.includes('server')) return 'Server';
    if (name.includes('laptop')) return 'Laptop';
    if (name.includes('desktop')) return 'Desktop';
    return 'Device';
  }

  trackByNodeId(index: number, node: Node): string {
    return node.id || node.ip;
  }

  getDeviceIcon(hostname: string): string {
    const name = hostname.toLowerCase();
    if (name.includes('router')) return 'fas fa-network-wired';
    if (name.includes('switch')) return 'fas fa-exchange-alt';
    if (name.includes('server')) return 'fas fa-server';
    if (name.includes('laptop')) return 'fas fa-laptop';
    if (name.includes('desktop')) return 'fas fa-desktop';
    return 'fas fa-desktop';
  }

  getActiveNodes(): number {
    return this.nodes.filter(node => node.status === 'UP').length;
  }

  getSNMPActiveNodes(): number {
    return this.nodes.filter(node => node.snmpRunning).length;
  }

  private applyFilters(): void {
    let filtered = [...this.nodes];

    // Apply search filter
    if (this.searchTerm.trim()) {
      const search = this.searchTerm.toLowerCase();
      filtered = filtered.filter(node => 
        node.hostname.toLowerCase().includes(search) ||
        node.ip.toLowerCase().includes(search)
      );
    }

    // Apply status filter
    switch (this.currentFilter) {
      case 'active':
        filtered = filtered.filter(node => node.status === 'UP');
        break;
      case 'inactive':
        filtered = filtered.filter(node => node.status === 'DOWN');
        break;
    }

    this.filteredNodes = filtered;
  }

   onSearch(): void {
    this.applyFilters();
  }

  setFilter(filter: 'all' | 'active' | 'inactive'): void {
    this.currentFilter = filter;
    this.applyFilters();
  }

  setViewMode(mode: 'cards' | 'table'): void {
    this.viewMode = mode;
  }


}
