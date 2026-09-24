import { Injectable, signal } from '@angular/core';

export type TipoNotificacion = 'exito' | 'error';

export interface Notificacion {
  mensaje: string;
  tipo: TipoNotificacion;
}

@Injectable({ providedIn: 'root' })
export class NotificacionService {
  notificacion = signal<Notificacion | null>(null);
  private timeoutId: ReturnType<typeof setTimeout> | undefined;

  mostrar(msg: string, tipo: TipoNotificacion = 'exito'): void {
    this.notificacion.set({ mensaje: msg, tipo });
    clearTimeout(this.timeoutId);
    this.timeoutId = setTimeout(() => this.notificacion.set(null), 3500);
  }
}