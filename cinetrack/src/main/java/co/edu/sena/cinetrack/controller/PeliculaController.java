package co.edu.sena.cinetrack.controller;

import co.edu.sena.cinetrack.model.Pelicula;
import co.edu.sena.cinetrack.service.PeliculaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/peliculas")
public class PeliculaController {

    @Autowired
    private PeliculaService peliculaService;

    // GET /peliculas -> 200
    @GetMapping
    public ResponseEntity<List<Pelicula>> obtenerTodas() {
        return ResponseEntity.ok(peliculaService.obtenerTodas());
    }

    // GET /peliculas/{id} -> 200 | 404
    @GetMapping("/{id}")
    public ResponseEntity<Pelicula> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(peliculaService.obtenerPorId(id)
                .orElseThrow(() -> new NoSuchElementException("No existe una película con id " + id)));
    }

    // POST /peliculas -> 201 | 400
    @PostMapping
    public ResponseEntity<Pelicula> guardar(@Valid @RequestBody Pelicula pelicula) {
        return ResponseEntity.status(HttpStatus.CREATED).body(peliculaService.guardar(pelicula));
    }

    // PUT /peliculas/{id} -> 200 | 400 | 404
    @PutMapping("/{id}")
    public ResponseEntity<Pelicula> actualizar(@PathVariable Long id, @Valid @RequestBody Pelicula pelicula) {
        return ResponseEntity.ok(peliculaService.actualizar(id, pelicula));
    }

    // DELETE /peliculas/{id} -> 204 | 404
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        peliculaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
