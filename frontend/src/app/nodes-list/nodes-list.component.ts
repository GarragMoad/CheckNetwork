import { Component, OnInit } from '@angular/core';
import { WebSocketService } from '../services/web-socket.service';
import { GetDashboardService } from '../services/get-dashboard.service';

@Component({
  selector: 'app-nodes-list',
  templateUrl: './nodes-list.component.html',
})
export class NodesListComponent implements OnInit {
  nodes: any[] = [];

  constructor(
    private webSocketService: WebSocketService,
    private getDashboardService: GetDashboardService
  ) {}

  ngOnInit(): void {
    this.webSocketService.nodes$.subscribe(nodes => {
      this.nodes = nodes;
      this.enrichWithGrafanaUrls();
    });
  }

  enrichWithGrafanaUrls(): void {
    this.nodes.forEach(node => {
      const title = `Interface Metrics - ${node.ip}`;
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
}
