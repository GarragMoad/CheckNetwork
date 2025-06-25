import { Component, OnInit } from '@angular/core';
import { WebSocketService } from '../services/web-socket.service';
import { GetDashboardService } from '../services/get-dashboard.service';
import { GuacamoleService } from '../services/guacamole.service';
import { MatDialog } from '@angular/material/dialog';
import { SnmpStatusService } from '../services/snmp-status.service';

@Component({
  selector: 'app-nodes-list',
  templateUrl: './nodes-list.component.html',
  styleUrls: ['./nodes-list.component.scss'],
})
export class NodesListComponent implements OnInit {
  nodes: any[] = [];
  snmpStatus: any[] = [];

  constructor(
    private webSocketService: WebSocketService,
    private getDashboardService: GetDashboardService,
    private guacamoleService: GuacamoleService,
    private dialog: MatDialog,
    private snmpStatusService: SnmpStatusService
  ) {}

  ngOnInit(): void {
    this.loadSnmpStatus();


    this.webSocketService.nodes$.subscribe(nodes => {
      this.nodes = nodes;
      console.log('Received nodes:', this.nodes);
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

}
