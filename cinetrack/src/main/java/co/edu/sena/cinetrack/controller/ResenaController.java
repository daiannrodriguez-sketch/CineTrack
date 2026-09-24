package co.edu.sena.cinetrack.controller;

import co.edu.sena.cinetrack.model.Resena;
import co.edu.sena.cinetrack.service.ResenaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/resenas")
public class ResenaController {

    @Autowired
    private ResenaService resenaService;

    @GetMapping
    public ResponseEntity<List<Resena>> obtenerTodas() {
        return ResponseEntity.ok(resenaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resena> obtenerPorId(@PathVariable Long id) {
        return resenaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /resenas/pelicula/{peliculaId} -> todas las reseñas de esa película
    @GetMapping("/pelicula/{peliculaId}")
    public ResponseEntity<List<Resena>> obtenerPorPelicula(@PathVariable Long peliculaId) {
        return ResponseEntity.ok(resenaService.obtenerPorPelicula(peliculaId));
    }

    @PostMapping
    public ResponseEntity<?> guardar(@Valid @RequestBody Resena resena) {
        try {
            Resena nueva = resenaService.guardar(resena);
            return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody Resena resena) {
        try {
            Resena actualizada = resenaService.actualizar(id, resena);
            return ResponseEntity.ok(actualizada);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            resenaService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}