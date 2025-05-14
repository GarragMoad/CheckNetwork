import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class GuacamoleService {
  private apiUrl = 'http://localhost:8081/api/guacamole'; 

  constructor(private http: HttpClient) {}

  login(username: string, password: string): Observable<any> {
  const headers = new HttpHeaders({ 'Content-Type': 'application/x-www-form-urlencoded' });
  const body = new HttpParams()
    .set('username', username)
    .set('password', password);

  return this.http.post(`${this.apiUrl}/login`, body.toString(), { headers });
}



  getConnectionUrl(connectionId: string, token: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/connect?connectionId=${connectionId}&token=${token}`);
  }
}
