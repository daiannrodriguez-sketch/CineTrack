package co.edu.sena.cinetrack.dto;

import java.util.List;

public class DashboardDTO {
    private long totalPeliculas;
    private long vistas;
    private long pendientes;
    private long viendo;
    private long alertas;
    private List<UltimaOperacionDTO> ultimasOperaciones;

    // Getters y setters de todos los campos
    public long getTotalPeliculas() { return totalPeliculas; }
    public void setTotalPeliculas(long totalPeliculas) { this.totalPeliculas = totalPeliculas; }

    public long getVistas() { return vistas; }
    public void setVistas(long vistas) { this.vistas = vistas; }

    public long getPendientes() { return pendientes; }
    public void setPendientes(long pendientes) { this.pendientes = pendientes; }

    public long getViendo() { return viendo; }
    public void setViendo(long viendo) { this.viendo = viendo; }

    public long getAlertas() { return alertas; }
    public void setAlertas(long alertas) { this.alertas = alertas; }

    public List<UltimaOperacionDTO> getUltimasOperaciones() { return ultimasOperaciones; }
    public void setUltimasOperaciones(List<UltimaOperacionDTO> ultimasOperaciones) { this.ultimasOperaciones = ultimasOperaciones; }
}