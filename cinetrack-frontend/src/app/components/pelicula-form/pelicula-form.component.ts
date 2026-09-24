import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { PeliculaService } from '../../services/pelicula.services';
import { TmdbService } from '../../services/tmdb.service';
import { NotificacionService } from '../../services/notificacion.service';
import { Pelicula } from '../../models/pelicula';
import { TmdbResultado } from '../../models/tmdbresultado';

@Component({
  selector: 'app-pelicula-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './pelicula-form.component.html'
})
export class PeliculaFormComponent implements OnInit {
  private peliculaService = inject(PeliculaService);
  private tmdbService = inject(TmdbService);
  private notificacionService = inject(NotificacionService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private cdr = inject(ChangeDetectorRef);

  editando = false;

  tituloBusqueda = '';
  resultadosTmdb: TmdbResultado[] = [];
  buscando = false;

  nuevaPelicula: Pelicula = this.peliculaVacia();

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.editando = true;
      const id = Number(idParam);
      this.peliculaService.buscar(id).subscribe({
        next: (p) => {
          this.nuevaPelicula = p;
          this.cdr.markForCheck();
        },
        error: () => {
          this.notificacionService.mostrar('❌ No se encontró esa película', 'error');
          this.router.navigate(['/peliculas']);
        }
      });
    }
  }

  private peliculaVacia(): Pelicula {
    return {
      titulo: '',
      resenaPersonal: '',
      genero: '',
      anio: 2024,
      sinopsis: '',
      imagenUrl: '',
      estado: 'PENDIENTE',
      calificacionPersonal: 5.0
    };
  }

  guardarPelicula(): void {
    if (this.editando && this.nuevaPelicula.id) {
      this.peliculaService.actualizar(this.nuevaPelicula.id, this.nuevaPelicula).subscribe({
        next: () => {
          this.notificacionService.mostrar('✨ Película actualizada al instante');
          this.router.navigate(['/peliculas']);
        },
        error: () => {
          this.notificacionService.mostrar('❌ Error al actualizar. Revisa tu conexión con el servidor.', 'error');
          this.cdr.markForCheck();
        }
      });
    } else {
      this.peliculaService.crear(this.nuevaPelicula).subscribe({
        next: () => {
          this.notificacionService.mostrar('✅ Película registrada con éxito');
          this.router.navigate(['/peliculas']);
        },
        error: () => {
          this.notificacionService.mostrar('❌ Error al guardar. Revisa tu conexión con el servidor.', 'error');
          this.cdr.markForCheck();
        }
      });
    }
  }

  cancelar(): void {
    this.router.navigate(['/peliculas']);
  }

  buscarEnTmdb(): void {
    if (!this.tituloBusqueda.trim()) return;

    this.buscando = true;
    this.tmdbService.buscar(this.tituloBusqueda).subscribe({
      next: (res) => {
        this.resultadosTmdb = res;
        this.buscando = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.notificacionService.mostrar('❌ TMDB no respondió. Puedes seguir llenando el formulario a mano.', 'error');
        this.buscando = false;
        this.cdr.markForCheck();
      }
    });
  }

  seleccionarResultadoTmdb(r: TmdbResultado): void {
    this.nuevaPelicula.titulo = r.titulo;
    this.nuevaPelicula.anio = r.anio;
    this.nuevaPelicula.sinopsis = r.sinopsis;
    this.nuevaPelicula.imagenUrl = r.imagenUrl;
    this.nuevaPelicula.genero = r.genero;
    this.resultadosTmdb = [];
    this.tituloBusqueda = '';
  }
}