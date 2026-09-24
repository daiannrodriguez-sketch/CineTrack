package co.edu.sena.cinetrack.service;

import co.edu.sena.cinetrack.repository.PeliculaRepository;
import co.edu.sena.cinetrack.model.Resena;
import co.edu.sena.cinetrack.repository.ResenaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ResenaService {

    @Autowired
    private ResenaRepository resenaRepository;

    @Autowired
    private PeliculaRepository peliculaRepository;

    public List<Resena> obtenerTodas() {
        return resenaRepository.findAll();
    }

    public Optional<Resena> obtenerPorId(Long id) {
        return resenaRepository.findById(id);
    }

    public List<Resena> obtenerPorPelicula(Long peliculaId) {
        return resenaRepository.findByPeliculaId(peliculaId);
    }

    public Resena guardar(Resena resena) {
        // Regla de negocio: la película referenciada debe existir de verdad
        if (!peliculaRepository.existsById(resena.getPeliculaId())) {
            throw new IllegalArgumentException("No existe una película con id " + resena.getPeliculaId());
        }
        return resenaRepository.save(resena);
    }

    public Resena actualizar(Long id, Resena resena) {
        if (!resenaRepository.existsById(id)) {
            throw new NoSuchElementException("No existe una reseña con id " + id);
        }
        if (!peliculaRepository.existsById(resena.getPeliculaId())) {
            throw new IllegalArgumentException("No existe una película con id " + resena.getPeliculaId());
        }
        resena.setId(id);
        return resenaRepository.save(resena);
    }

    public void eliminar(Long id) {
        if (!resenaRepository.existsById(id)) {
            throw new NoSuchElementException("No existe una reseña con id " + id);
        }
        resenaRepository.deleteById(id);
    }
}