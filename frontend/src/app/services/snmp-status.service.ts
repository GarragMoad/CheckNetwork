// snmp-status.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthServiceService } from './auth-service.service';

@Injectable({
  providedIn: 'root'
})
export class SnmpStatusService {
  private apiUrl = 'http://localhost:8081/api/snmp/status'; 

  constructor(private http: HttpClient , private authService : AuthServiceService) { }

  getSnmpStatus(): Observable<any[]> {
    const token =  this.authService.getToken()
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    return this.http.get<any[]>(this.apiUrl, { headers });
  }
}