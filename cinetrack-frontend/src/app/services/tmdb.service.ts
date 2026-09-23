import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TmdbResultado } from '../models/tmdbresultado';

@Injectable({ providedIn: 'root' })
export class TmdbService {

  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/tmdb';

  // GET /tmdb/buscar?titulo=...
  buscar(titulo: string): Observable<TmdbResultado[]> {
    return this.http.get<TmdbResultado[]>(`${this.apiUrl}/buscar`, {
      params: { titulo }
    });
  }
}