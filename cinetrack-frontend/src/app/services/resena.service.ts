import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Resena } from '../models/resena';

@Injectable({ providedIn: 'root' })
export class ResenaService {

  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/resenas';

  obtenerPorPelicula(peliculaId: number): Observable<Resena[]> {
    return this.http.get<Resena[]>(`${this.apiUrl}/pelicula/${peliculaId}`);
  }

  crear(resena: Resena): Observable<Resena> {
    return this.http.post<Resena>(this.apiUrl, resena);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}