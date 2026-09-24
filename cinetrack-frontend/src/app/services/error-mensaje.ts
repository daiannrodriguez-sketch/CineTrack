import { HttpErrorResponse } from '@angular/common/http';

export const MSG_SIN_SERVIDOR =
  'No se pudo conectar con el servidor. Verifica que Spring Boot esté corriendo en http://localhost:8080.';

/**
 * Convierte un error HTTP en un mensaje comprensible para el usuario.
 * - status 0  -> el navegador no pudo llegar al backend (apagado, red, CORS)
 * - 400/404/503 -> el backend manda {"mensaje": "..."} con el motivo real
 */
export function mensajeDeError(err: unknown, porDefecto: string): string {
  if (!(err instanceof HttpErrorResponse)) {
    return porDefecto;
  }
  if (err.status === 0) {
    return MSG_SIN_SERVIDOR;
  }
  const mensajeServidor: string | undefined =
    typeof err.error === 'string' ? err.error : err.error?.mensaje;

  if (err.status === 400 || err.status === 404 || err.status === 503) {
    return mensajeServidor ?? porDefecto;
  }
  return porDefecto;
}
