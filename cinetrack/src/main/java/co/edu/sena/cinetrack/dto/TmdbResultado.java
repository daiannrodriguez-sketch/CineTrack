package co.edu.sena.cinetrack.dto;

public class TmdbResultado {
    private String titulo;
    private int anio;
    private String sinopsis;
    private String imagenUrl;
    public String genero;

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }

    public String getSinopsis() { return sinopsis; }
    public void setSinopsis(String sinopsis) { this.sinopsis = sinopsis; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public String getGenero() { return genero; }
public void setGenero(String genero) { this.genero = genero; }

}