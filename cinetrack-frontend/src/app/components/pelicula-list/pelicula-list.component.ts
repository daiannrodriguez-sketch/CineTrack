import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { PeliculaService } from '../../services/pelicula.services';
import { NotificacionService } from '../../services/notificacion.service';
import { Pelicula } from '../../models/pelicula';
import { mensajeDeError } from '../../services/error-mensaje';

@Component({
  selector: 'app-pelicula-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './pelicula-list.component.html'
})
export class PeliculaListComponent implements OnInit {
  private peliculaService = inject(PeliculaService);
  private notificacionService = inject(NotificacionService);
  private router = inject(Router);
  private cdr = inject(ChangeDetectorRef);

  peliculas: Pelicula[] = [];
  cargando = true;
  backendCaido = false;

  ngOnInit(): void {
    this.cargarPeliculas();
  }

  cargarPeliculas(): void {
    this.cargando = true;
    this.backendCaido = false;
    this.peliculaService.listar().subscribe({
      next: (datos) => {
        this.peliculas = datos;
        this.cargando = false;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('Error conectando con Spring Boot:', err);
        this.cargando = false;
        this.backendCaido = true;
        this.cdr.markForCheck();
      }
    });
  }

  verDetalle(id?: number): void {
    if (!id) return;
    this.router.navigate(['/peliculas', id]);
  }

  editar(id?: number): void {
    if (!id) return;
    this.router.navigate(['/peliculas', id, 'editar']);
  }

  eliminarPelicula(id?: number): void {
    if (!id) return;
    if (confirm('¿Está seguro de eliminar esta película?')) {
      this.peliculaService.eliminar(id).subscribe({
        next: () => {
          this.peliculas = this.peliculas.filter(p => p.id !== id);
          this.notificacionService.mostrar('🗑️ Película eliminada');
          this.cdr.markForCheck();
        },
        error: (err) => {
          this.notificacionService.mostrar(
            '❌ ' + mensajeDeError(err, 'No se pudo eliminar la película.'), 'error');
          this.cdr.markForCheck();
        }
      });
    }
  }
}