import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { PeliculaService } from '../../services/pelicula.services';
import { ResenaService } from '../../services/resena.service';
import { NotificacionService } from '../../services/notificacion.service';
import { Pelicula } from '../../models/pelicula';
import { Resena } from '../../models/resena';

@Component({
  selector: 'app-pelicula-detalle',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './pelicula-detalle.component.html'
})
export class PeliculaDetalleComponent implements OnInit {
  private peliculaService = inject(PeliculaService);
  private resenaService = inject(ResenaService);
  private notificacionService = inject(NotificacionService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private cdr = inject(ChangeDetectorRef);

  pelicula: Pelicula | null = null;
  resenas: Resena[] = [];
  peliculaId!: number;

  mostrandoFormResena = false;
  nuevaResena: Resena = this.resenaVacia();

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (!idParam) {
      this.router.navigate(['/peliculas']);
      return;
    }
    this.peliculaId = Number(idParam);
    this.cargarPelicula();
    this.cargarResenas();
  }

  private resenaVacia(): Resena {
    return {
      peliculaId: this.peliculaId,
      fecha: new Date().toISOString().substring(0, 10),
      calificacion: 5,
      descripcion: ''
    };
  }

  cargarPelicula(): void {
    this.peliculaService.buscar(this.peliculaId).subscribe({
      next: (p) => {
        this.pelicula = p;
        this.cdr.markForCheck();
      },
      error: () => {
        this.notificacionService.mostrar('❌ No se encontró esa película', 'error');
        this.router.navigate(['/peliculas']);
      }
    });
  }

  cargarResenas(): void {
    this.resenaService.obtenerPorPelicula(this.peliculaId).subscribe({
      next: (res) => {
        this.resenas = res;
        this.cdr.markForCheck();
      },
      error: () => {
        console.error('Error al cargar reseñas');
        this.cdr.markForCheck();
      }
    });
  }

  editarPelicula(): void {
    this.router.navigate(['/peliculas', this.peliculaId, 'editar']);
  }

  eliminarPelicula(): void {
    if (confirm('¿Está seguro de eliminar esta película?')) {
      this.peliculaService.eliminar(this.peliculaId).subscribe({
        next: () => {
          this.notificacionService.mostrar('🗑️ Película eliminada');
          this.router.navigate(['/peliculas']);
        },
        error: () => console.error('Error al eliminar')
      });
    }
  }

  volver(): void {
    this.router.navigate(['/peliculas']);
  }

  mostrarFormResena(): void {
    this.nuevaResena = this.resenaVacia();
    this.mostrandoFormResena = true;
  }

  guardarResena(): void {
    this.resenaService.crear(this.nuevaResena).subscribe({
      next: () => {
        this.notificacionService.mostrar('📝 Reseña agregada');
        this.mostrandoFormResena = false;
        this.cargarResenas();
      },
      error: () => {
        this.notificacionService.mostrar('❌ Error al guardar la reseña. Revisa tu conexión con el servidor.', 'error');
        this.cdr.markForCheck();
      }
    });
  }

  eliminarResena(id?: number): void {
    if (!id) return;
    this.resenaService.eliminar(id).subscribe({
      next: () => this.cargarResenas(),
      error: () => console.error('Error al eliminar reseña')
    });
  }
}