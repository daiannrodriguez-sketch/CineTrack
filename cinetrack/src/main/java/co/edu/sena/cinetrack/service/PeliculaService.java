package co.edu.sena.cinetrack.service;

import co.edu.sena.cinetrack.model.EstadoPelicula;
import co.edu.sena.cinetrack.model.Pelicula;
import co.edu.sena.cinetrack.repository.PeliculaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
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
        if (peliculaRepository.existsByTituloAndAño(pelicula.getTitulo(), pelicula.getAnio())) {
            throw new IllegalArgumentException("La película ya existe en tu catálogo");
        }

        validarReglasComunes(pelicula);

        return peliculaRepository.save(pelicula);
    }

    // NUEVO: método propio para actualizar (antes el PUT usaba guardar() por error)
    public Pelicula actualizar(Long id, Pelicula pelicula) {
        if (!peliculaRepository.existsById(id)) {
            throw new NoSuchElementException("No existe una película con id " + id);
        }

        if (peliculaRepository.existsByTituloAndAñoAndIdNot(pelicula.getTitulo(), pelicula.getAnio(), id)) {
            throw new IllegalArgumentException("Ya existe otra película con ese título y año");
        }

        validarReglasComunes(pelicula);

        pelicula.setId(id);
        return peliculaRepository.save(pelicula);
    }

    public void eliminar(Long id) {
        // NUEVO: verifica que exista antes de borrar (evita 500 en vez de 404)
        if (!peliculaRepository.existsById(id)) {
            throw new NoSuchElementException("No existe una película con id " + id);
        }
        peliculaRepository.deleteById(id);
    }

    // NUEVO: se extrajeron las reglas 2 y 3 aquí para reusarlas en guardar() y actualizar()
    private void validarReglasComunes(Pelicula pelicula) {
        // Regla 2: Calificación en rango válido de 1.0 a 5.0
        if (pelicula.getCalificacionPersonal() < 1.0 || pelicula.getCalificacionPersonal() > 5.0) {
            throw new IllegalArgumentException("La calificación debe estar entre 1.0 y 5.0");
        }

        // Regla 3: No marcar como VISTA sin calificación
        if (pelicula.getEstado() == EstadoPelicula.VISTA && pelicula.getCalificacionPersonal() <= 0) {
            throw new IllegalArgumentException("Para marcar como VISTA debes asignar una calificación");
        }
    }
}