package co.edu.sena.cinetrack.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "peliculas")
public class Pelicula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El título es obligatorio")
    private String titulo;

    private String genero;
    private String resenaPersonal;
    @Min(value = 1888, message = "El año debe ser 1888 o posterior")
    @Max(value = 2100, message = "El año no es válido")
    private int anio;

    @Column(length = 1000)
    private String sinopsis;

    private String imagenUrl;

    @NotNull(message = "El estado es obligatorio")
    @Enumerated(EnumType.STRING)
    private EstadoPelicula estado; // PENDIENTE, VIENDO, VISTA

    // 0 = sin calificar (permitido en PENDIENTE / VIENDO). Si se califica: 1.0 a 5.0
    @DecimalMin(value = "0.0", message = "La calificación no puede ser negativa")
    @DecimalMax(value = "5.0", message = "La calificación máxima es 5.0")
    private double calificacionPersonal;

    public Pelicula() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public String getSinopsis() { return sinopsis; }
    public void setSinopsis(String sinopsis) { this.sinopsis = sinopsis; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public EstadoPelicula getEstado() { return estado; }
    public void setEstado(EstadoPelicula estado) { this.estado = estado; }

    public double getCalificacionPersonal() { return calificacionPersonal; }
    public void setCalificacionPersonal(double calificacionPersonal) { this.calificacionPersonal = calificacionPersonal; }

    public String getResenaPersonal() { return resenaPersonal; }
    public void setResenaPersonal(String resenaPersonal) { this.resenaPersonal = resenaPersonal; }

    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }
}