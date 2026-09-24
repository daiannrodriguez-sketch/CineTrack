package co.edu.sena.cinetrack.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;
import java.time.LocalDate;

@Entity
@Table(name = "resenas")
public class Resena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La reseña debe estar asociada a una película")
    private Long peliculaId;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @DecimalMin(value = "1.0", message = "La calificación mínima es 1.0")
    @DecimalMax(value = "5.0", message = "La calificación máxima es 5.0")
    private double calificacion;

    private String descripcion;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPeliculaId() { return peliculaId; }
    public void setPeliculaId(Long peliculaId) { this.peliculaId = peliculaId; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public double getCalificacion() { return calificacion; }
    public void setCalificacion(double calificacion) { this.calificacion = calificacion; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}