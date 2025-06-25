// snmp-status.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SnmpStatusService {
  private apiUrl = 'http://localhost:8081/api/snmp/status'; 

  constructor(private http: HttpClient) { }

  getSnmpStatus(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }
}