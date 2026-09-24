package co.edu.sena.cinetrack.dto;

public class UltimaOperacionDTO {
    private String peliculaTitulo;
    private double calificacion;
    private String fecha;
    private String descripcion;

    public UltimaOperacionDTO(String peliculaTitulo, double calificacion, String fecha, String descripcion) {
        this.peliculaTitulo = peliculaTitulo;
        this.calificacion = calificacion;
        this.fecha = fecha;
        this.descripcion = descripcion;
    }

    public String getPeliculaTitulo() { return peliculaTitulo; }
    public double getCalificacion() { return calificacion; }
    public String getFecha() { return fecha; }
    public String getDescripcion() { return descripcion; }
}