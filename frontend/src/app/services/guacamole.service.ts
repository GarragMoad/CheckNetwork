import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class GuacamoleService {
  private apiUrl = 'http://localhost:8081/api/guacamole'; 

  constructor(private http: HttpClient) {}
  
  getConnectionUrlByIp(ip: string): Observable<{ url: string }> {
    return this.http.get<{ url: string }>(`${this.apiUrl}/connect/by-ip?ip=${ip}`);
  }
}
