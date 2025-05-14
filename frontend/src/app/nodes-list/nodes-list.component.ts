import { Component, OnInit } from '@angular/core';
import { WebSocketService } from '../services/web-socket.service';
import { GetDashboardService } from '../services/get-dashboard.service';
import { GuacamoleService } from '../services/guacamole.service';
import { MatDialog } from '@angular/material/dialog';
import { GuacamoleDialogComponent } from '../components/guacamole/guacamole-dialog.component';

@Component({
  selector: 'app-nodes-list',
  templateUrl: './nodes-list.component.html',
})
export class NodesListComponent implements OnInit {
  nodes: any[] = [];

  constructor(
    private webSocketService: WebSocketService,
    private getDashboardService: GetDashboardService,
    private guacamoleService: GuacamoleService,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.webSocketService.nodes$.subscribe(nodes => {
      this.nodes = nodes;
      console.log('Received nodes:', this.nodes);
      this.enrichWithGrafanaUrls();
    });
  }

  enrichWithGrafanaUrls(): void {
    this.nodes.forEach(node => {
      //const title = `Interface Metrics - ${node.ip}`;
      const title = `toto`;
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


}
