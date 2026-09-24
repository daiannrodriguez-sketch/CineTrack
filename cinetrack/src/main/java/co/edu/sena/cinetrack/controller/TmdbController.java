package co.edu.sena.cinetrack.controller;

import co.edu.sena.cinetrack.dto.TmdbResultado;
import co.edu.sena.cinetrack.service.TmdbService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tmdb")
public class TmdbController {

    private final TmdbService tmdbService;

    public TmdbController(TmdbService tmdbService) {
        this.tmdbService = tmdbService;
    }

    // GET /tmdb/buscar?titulo=matrix -> 200 | 400 | 503 (TMDB no disponible)
    @GetMapping("/buscar")
    public ResponseEntity<List<TmdbResultado>> buscar(@RequestParam String titulo) {
        return ResponseEntity.ok(tmdbService.buscarPeliculas(titulo));
    }
}
