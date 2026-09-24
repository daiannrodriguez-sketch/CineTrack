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
        return ResponseEntity.ok(resenaService.obtenerPorId(id)
                .orElseThrow(() -> new NoSuchElementException("No existe una reseña con id " + id)));
    }

    // GET /resenas/pelicula/{peliculaId} -> todas las reseñas de esa película
    @GetMapping("/pelicula/{peliculaId}")
    public ResponseEntity<List<Resena>> obtenerPorPelicula(@PathVariable Long peliculaId) {
        return ResponseEntity.ok(resenaService.obtenerPorPelicula(peliculaId));
    }

    @PostMapping
    public ResponseEntity<Resena> guardar(@Valid @RequestBody Resena resena) {
        return ResponseEntity.status(HttpStatus.CREATED).body(resenaService.guardar(resena));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Resena> actualizar(@PathVariable Long id, @Valid @RequestBody Resena resena) {
        return ResponseEntity.ok(resenaService.actualizar(id, resena));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        resenaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
