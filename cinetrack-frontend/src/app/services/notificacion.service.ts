import { Injectable, signal } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class NotificacionService {
  mensaje = signal('');
  private timeoutId: ReturnType<typeof setTimeout> | undefined;

  mostrar(msg: string): void {
    this.mensaje.set(msg);
    clearTimeout(this.timeoutId);
    this.timeoutId = setTimeout(() => this.mensaje.set(''), 3000);
  }
}