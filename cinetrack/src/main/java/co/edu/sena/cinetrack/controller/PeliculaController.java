package co.edu.sena.cinetrack.controller;

import co.edu.sena.cinetrack.model.Pelicula;
import co.edu.sena.cinetrack.service.PeliculaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/peliculas")
public class PeliculaController {

    @Autowired
    private PeliculaService peliculaService;

    // GET /peliculas
    @GetMapping
    public ResponseEntity<List<Pelicula>> obtenerTodas() {
        return ResponseEntity.ok(peliculaService.obtenerTodas());
    }

    // GET /peliculas/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Pelicula> obtenerPorId(@PathVariable Long id) {
        return peliculaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /peliculas
    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Pelicula pelicula) {
        try {
            Pelicula nuevaPelicula = peliculaService.guardar(pelicula);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaPelicula);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE /peliculas/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        peliculaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
public ResponseEntity<Pelicula> actualizar(@PathVariable Long id, @RequestBody Pelicula pelicula) {
    pelicula.setId(id);
    Pelicula actualizada = peliculaService.guardar(pelicula); // O peliculaService.actualizar(id, pelicula)
    return ResponseEntity.ok(actualizada);
}
}