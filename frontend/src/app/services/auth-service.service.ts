import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthServiceService {
  private apiUrl = 'http://localhost:8081/api/auth/login';
  private isAuthenticated = false;

  constructor(private http: HttpClient ,private router: Router ) { }

  login(credentials: { username: string, password: string }): Observable<any> {
    return this.http.post<any>(this.apiUrl, credentials);
  }

  loginSuccess(): void {
    this.isAuthenticated = true;
  }

  saveToken(token: string): void {
    localStorage.setItem('authToken', token);
  }

  saveUserName(userName: string): void {
    localStorage.setItem('userName', userName);
  }

  getUserName(): string | null {
    return localStorage.getItem('userName');
  }

  getToken(): string | null {
    return localStorage.getItem('authToken');
  }

    logout(): void {
    localStorage.removeItem('authToken');
    this.isAuthenticated = false;
  }

  isTokenExpired(token: string): boolean {
    if (!token) return true;
    
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.exp < Date.now() / 1000;
    } catch {
      return true;
    }
  }

  isLoggedIn(): boolean {
  const token = this.getToken();
  return !!token && !this.isTokenExpired(token);
  }
}
