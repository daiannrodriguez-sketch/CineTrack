import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Pelicula } from '../models/pelicula';

@Injectable({ providedIn: 'root' })
export class PeliculaService {

  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/peliculas'; // Endpoint de Spring Boot

  // GET /peliculas
  listar(): Observable<Pelicula[]> {
    return this.http.get<Pelicula[]>(this.apiUrl);
  }

  // GET /peliculas/{id}
  buscar(id: number): Observable<Pelicula> {
    return this.http.get<Pelicula>(`${this.apiUrl}/${id}`);
  }

  // POST /peliculas
  crear(pelicula: Pelicula): Observable<Pelicula> {
    return this.http.post<Pelicula>(this.apiUrl, pelicula);
  }

  // DELETE /peliculas/{id}
  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
  // Reto 9: PUT /peliculas/{id}
  actualizar(id: number, pelicula: Pelicula): Observable<Pelicula> {
  return this.http.put<Pelicula>(`${this.apiUrl}/${id}`, pelicula);
  }
}