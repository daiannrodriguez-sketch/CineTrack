package co.edu.sena.cinetrack.repository;

import co.edu.sena.cinetrack.model.Pelicula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface PeliculaRepository extends JpaRepository<Pelicula, Long> {
    // Método para validar la Regla de Negocio 1 (Evitar duplicados) al CREAR
    boolean existsByTituloAndAnio(String titulo, int anio);

    // NUEVO: igual, pero excluyendo el propio registro (necesario al ACTUALIZAR)
    boolean existsByTituloAndAnioAndIdNot(String titulo, int anio, Long id);
}