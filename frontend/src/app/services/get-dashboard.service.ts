import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthServiceService } from './auth-service.service';

@Injectable({
  providedIn: 'root'
})
export class Panel {
  title: string | undefined;
  type: string | undefined;
  targets: any[] | undefined;
  fieldConfig?: any;
}
@Injectable({
  providedIn: 'root'
})
export class GetDashboardService {

  private apiUrl = 'http://localhost:8081/api/dashboards/search';

  constructor(private http: HttpClient , private authService :  AuthServiceService) {}

  getDashboardUrl(title: string): Observable<string> {
  const token = this.authService.getToken(); // Assure-toi que le nom correspond bien
  const headers = new HttpHeaders({
    'Authorization': `Bearer ${token}`
  });

  const url = `${this.apiUrl}?title=${encodeURIComponent(title)}`;
  console.log('URL:', url);

  return this.http.get<string>(url, { headers });
}

}
