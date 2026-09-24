package co.edu.sena.cinetrack.controller;

import co.edu.sena.cinetrack.dto.DashboardDTO;
import co.edu.sena.cinetrack.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public ResponseEntity<DashboardDTO> obtener() {
        return ResponseEntity.ok(dashboardService.obtenerEstadisticas());
    }
}