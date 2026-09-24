import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DashboardStats } from '../models/dashboard';

@Injectable({ providedIn: 'root' })
export class DashboardService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/dashboard';

  obtener(): Observable<DashboardStats> {
    return this.http.get<DashboardStats>(this.apiUrl);
  }
}