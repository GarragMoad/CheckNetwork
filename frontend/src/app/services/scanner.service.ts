import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface NetworkNode {
  id: number;
  ip: string;
  hostname: string;
  status: string;
}

@Injectable({
  providedIn: 'root'
})
export class ScannerService {
  private apiUrl = 'http://localhost:8081/api/network/scan';

  constructor(private http: HttpClient) { }

  scanNetwork(subnet: string): Observable<NetworkNode[]> {
    return this.http.get<NetworkNode[]>(`${this.apiUrl}?subnet=${subnet}`);
  }
}
