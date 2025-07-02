import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class GrafanaServiceJson {

  private apiUrl = 'http://localhost:8081/api/dashboards/dashboard-json';

  constructor(private http: HttpClient) {}

  getDashboardJson(title: string) {
    return this.http.get<any>(this.apiUrl, {
      params: { title }
    });
  }
}
