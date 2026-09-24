package co.edu.sena.cinetrack.exception;

import co.edu.sena.cinetrack.service.ServicioExternoNoDisponibleException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Convierte las excepciones en respuestas JSON con un mensaje legible: {"mensaje": "..."}.
 * Así Angular puede mostrarle al usuario el motivo real del error.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400: reglas de negocio incumplidas
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> reglaDeNegocio(IllegalArgumentException e) {
        return respuesta(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    // 400: validaciones de @Valid (campos vacíos, rangos, etc.)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> validacion(MethodArgumentNotValidException e) {
        String mensaje = e.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getDefaultMessage())
                .collect(Collectors.joining(". "));
        return respuesta(HttpStatus.BAD_REQUEST, mensaje);
    }

    // 400: JSON mal formado o valor de enum inválido
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> cuerpoInvalido(HttpMessageNotReadableException e) {
        return respuesta(HttpStatus.BAD_REQUEST, "Los datos enviados no tienen un formato válido");
    }

    // 404: id inexistente
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> noEncontrado(NoSuchElementException e) {
        return respuesta(HttpStatus.NOT_FOUND, e.getMessage());
    }

    // 503: API externa caída
    @ExceptionHandler(ServicioExternoNoDisponibleException.class)
    public ResponseEntity<Map<String, String>> servicioExterno(ServicioExternoNoDisponibleException e) {
        return respuesta(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
    }

    private ResponseEntity<Map<String, String>> respuesta(HttpStatus status, String mensaje) {
        return ResponseEntity.status(status).body(Map.of("mensaje", mensaje));
    }
}
