import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { PeliculaService } from '../../services/pelicula.services';
import { NotificacionService } from '../../services/notificacion.service';
import { Pelicula } from '../../models/pelicula';

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

  ngOnInit(): void {
    this.cargarPeliculas();
  }

  cargarPeliculas(): void {
    this.peliculaService.listar().subscribe({
      next: (datos) => {
        this.peliculas = datos;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('Error conectando con Spring Boot:', err);
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
        error: () => {
          console.error('Error al eliminar');
          this.cdr.markForCheck();
        }
      });
    }
  }
}