import { inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, map, of, switchMap, timer } from 'rxjs';

/** Comprueba periódicamente si Spring Boot responde (para el indicador del navbar). */
@Injectable({ providedIn: 'root' })
export class EstadoBackendService {
  private http = inject(HttpClient);

  conectado = signal<boolean | null>(null); // null = comprobando

  constructor() {
    timer(0, 10000)
      .pipe(
        switchMap(() =>
          this.http.get('http://localhost:8080/dashboard').pipe(
            map(() => true),
            catchError(() => of(false))
          )
        )
      )
      .subscribe((ok) => this.conectado.set(ok));
  }
}
