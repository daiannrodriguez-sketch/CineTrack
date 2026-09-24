package co.edu.sena.cinetrack.service;

import co.edu.sena.cinetrack.dto.DashboardDTO;
import co.edu.sena.cinetrack.dto.UltimaOperacionDTO;
import co.edu.sena.cinetrack.model.EstadoPelicula;
import co.edu.sena.cinetrack.model.Pelicula;
import co.edu.sena.cinetrack.model.Resena;
import co.edu.sena.cinetrack.repository.PeliculaRepository;
import co.edu.sena.cinetrack.repository.ResenaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private PeliculaRepository peliculaRepository;

    @Autowired
    private ResenaRepository resenaRepository;

    public DashboardDTO obtenerEstadisticas() {
        DashboardDTO dto = new DashboardDTO();

        dto.setTotalPeliculas(peliculaRepository.count());
        dto.setVistas(peliculaRepository.countByEstado(EstadoPelicula.VISTA));
        dto.setPendientes(peliculaRepository.countByEstado(EstadoPelicula.PENDIENTE));
        dto.setViendo(peliculaRepository.countByEstado(EstadoPelicula.VIENDO));
        dto.setAlertas(peliculaRepository.peliculasVistasSinResena().size());

        List<Resena> ultimas = resenaRepository.findTop5ByOrderByIdDesc();
        List<UltimaOperacionDTO> operaciones = ultimas.stream()
                .map(r -> {
                    String titulo = peliculaRepository.findById(r.getPeliculaId())
                            .map(Pelicula::getTitulo)
                            .orElse("Película eliminada");
                    return new UltimaOperacionDTO(titulo, r.getCalificacion(), r.getFecha().toString(), r.getDescripcion());
                })
                .collect(Collectors.toList());
        dto.setUltimasOperaciones(operaciones);

        return dto;
    }
}