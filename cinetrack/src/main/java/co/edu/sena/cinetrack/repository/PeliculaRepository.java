package co.edu.sena.cinetrack.repository;

import co.edu.sena.cinetrack.model.EstadoPelicula;
import co.edu.sena.cinetrack.model.Pelicula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import co.edu.sena.cinetrack.model.EstadoPelicula;
import org.springframework.data.jpa.repository.Query;
import java.util.List;


@Repository
public interface PeliculaRepository extends JpaRepository<Pelicula, Long> {
    // Método para validar la Regla de Negocio 1 (Evitar duplicados) al CREAR
    boolean existsByTituloAndAnio(String titulo, int anio);

    // NUEVO: igual, pero excluyendo el propio registro (necesario al ACTUALIZAR)
    boolean existsByTituloAndAnioAndIdNot(String titulo, int anio, Long id);

    long countByEstado(EstadoPelicula estado);

    @Query("SELECT p FROM Pelicula p WHERE p.estado = 'VISTA' AND p.id NOT IN (SELECT r.peliculaId FROM Resena r)")
    List<Pelicula> peliculasVistasSinResena();

}