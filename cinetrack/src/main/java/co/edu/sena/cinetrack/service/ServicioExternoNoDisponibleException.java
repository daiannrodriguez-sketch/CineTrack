package co.edu.sena.cinetrack.service;

/** Se lanza cuando la API externa (TMDB) no responde o no está configurada. */
public class ServicioExternoNoDisponibleException extends RuntimeException {
    public ServicioExternoNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}
