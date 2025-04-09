// web-socket.service.ts
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Client } from '@stomp/stompjs';
import * as SockJS from 'sockjs-client';

export interface NetworkNode {
  ip: string;
  host: string;
  status: string;
  
}

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private stompClient!: Client;
  public nodes$ = new BehaviorSubject<NetworkNode[]>([]);

  constructor() {
    this.initializeWebSocketConnection();
  }

  private initializeWebSocketConnection() {
    this.stompClient = new Client({
      webSocketFactory: () => new SockJS('http://localhost:8081/ws'),
      reconnectDelay: 5000,
      debug: (str) => console.log(str)
    });

    this.stompClient.onConnect = () => {
      this.stompClient.subscribe('/topic/network-status', (message) => {
        const nodes = JSON.parse(message.body);
        this.nodes$.next(nodes);
      });
      this.requestNetworkStatus();
    };

    this.stompClient.onStompError = (frame) => {
      console.error('Broker reported error: ' + frame.headers['message']);
      console.error('Additional details: ' + frame.body);
    };

    this.stompClient.activate();
  }

  requestNetworkStatus() {
    this.stompClient.publish({
      destination: '/app/request-network-status',
      body: ''
    });
  }
}