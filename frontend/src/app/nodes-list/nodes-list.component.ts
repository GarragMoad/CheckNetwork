// nodes-list.component.ts
import { Component, OnInit } from '@angular/core';
import { WebSocketService } from '../services/web-socket.service';


@Component({
  selector: 'app-nodes-list',
  templateUrl: './nodes-list.component.html',
  styleUrls: ['./nodes-list.component.scss']
})
export class NodesListComponent implements OnInit {
  nodes: any[] = [];

  constructor(private webSocketService: WebSocketService) {}

  ngOnInit(): void {
    this.webSocketService.nodes$.subscribe(nodes => {
      this.nodes = nodes;
      console.log('Nodes updated:', nodes);
    });
  }

  refresh(): void {
    this.webSocketService.requestNetworkStatus();
  }
}