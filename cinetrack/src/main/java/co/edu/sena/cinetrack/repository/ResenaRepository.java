package co.edu.sena.cinetrack.repository;

import co.edu.sena.cinetrack.model.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Long> {
    // Todas las reseñas de una película específica
    List<Resena> findByPeliculaId(Long peliculaId);
    List<Resena> findTop5ByOrderByIdDesc();
}