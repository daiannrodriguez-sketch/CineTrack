package co.edu.sena.cinetrack.service;

import co.edu.sena.cinetrack.model.EstadoPelicula;
import co.edu.sena.cinetrack.model.Pelicula;
import co.edu.sena.cinetrack.repository.PeliculaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PeliculaService {

    @Autowired
    private PeliculaRepository peliculaRepository;

    public List<Pelicula> obtenerTodas() {
        return peliculaRepository.findAll();
    }

    public Optional<Pelicula> obtenerPorId(Long id) {
        return peliculaRepository.findById(id);
    }

    public Pelicula guardar(Pelicula pelicula) {
        // Regla 1: No registrar títulos duplicados con el mismo año
        if (peliculaRepository.existsByTituloAndAño(pelicula.getTitulo(), pelicula.getAño())) {
            throw new IllegalArgumentException("La película ya existe en tu catálogo");
        }

        // Regla 2: Calificación en rango válido de 1.0 a 5.0
        if (pelicula.getCalificacionPersonal() < 1.0 || pelicula.getCalificacionPersonal() > 5.0) {
            throw new IllegalArgumentException("La calificación debe estar entre 1.0 y 5.0");
        }

        // Regla 3: No marcar como VISTA sin calificación
        if (pelicula.getEstado() == EstadoPelicula.VISTA && pelicula.getCalificacionPersonal() <= 0) {
            throw new IllegalArgumentException("Para marcar como VISTA debes asignar una calificación");
        }

        return peliculaRepository.save(pelicula);
    }

    public void eliminar(Long id) {
        peliculaRepository.deleteById(id);
    }
}