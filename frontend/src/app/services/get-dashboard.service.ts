import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class GetDashboardService {

  private apiUrl = 'http://localhost:8081/api/dashboards/search';

  constructor(private http: HttpClient) {}

  getDashboardUrl(title: string): Observable<string> {
    const url = `${this.apiUrl}?title=${encodeURIComponent(title)}`;
    console.log('URL:', url);
    return this.http.get<string>(url);
  }
}
