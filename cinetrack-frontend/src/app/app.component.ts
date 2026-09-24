import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PeliculaService } from './services/pelicula.services';
import { Pelicula } from './models/pelicula';
import { TmdbService } from './services/tmdb.service';
import { TmdbResultado } from './models/tmdbresultado';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.html'
})
export class AppComponent implements OnInit {
  peliculaService = inject(PeliculaService);
  tmdbService = inject(TmdbService);
  private cdr = inject(ChangeDetectorRef);

  peliculas: Pelicula[] = [];
  editando: boolean = false;
  mensajeNotificacion: string = '';

  tituloBusqueda: string = '';
  resultadosTmdb: TmdbResultado[] = [];
  buscando: boolean = false;

  nuevaPelicula: Pelicula = {
    titulo: '',
    reseñaPersonal: '',
    genero: '',
    anio: 2024,
    sinopsis: '',
    imagenUrl: '',
    estado: 'PENDIENTE',
    calificacionPersonal: 5.0
  };

  ngOnInit(): void {
    this.cargarPeliculas();
  }

  cargarPeliculas(): void {
    this.peliculaService.listar().subscribe({
      next: (datos: Pelicula[]) => {
        this.peliculas = datos;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('Error conectando con Spring Boot:', err);
        this.cdr.markForCheck();
      }
    });
  }

  guardarPelicula(): void {
    if (this.editando && this.nuevaPelicula.id) {
      // PUT (Reto 9): Actualización
      this.peliculaService.actualizar(this.nuevaPelicula.id, this.nuevaPelicula).subscribe({
        next: (res) => {
          const index = this.peliculas.findIndex(p => p.id === res.id);
          if (index !== -1) this.peliculas[index] = res;
          this.mostrarNotificacion('✨ Película actualizada al instante');
          this.limpiarFormulario();
          this.cdr.markForCheck();
        },
        error: () => {
          this.mostrarNotificacion('❌ Error al actualizar');
          this.cdr.markForCheck();
        }
      });
    } else {
      // POST (Reto 8): Creación
      this.peliculaService.crear(this.nuevaPelicula).subscribe({
        next: (res) => {
          this.peliculas.push(res);
          this.mostrarNotificacion('✅ Película registrada con éxito');
          this.limpiarFormulario();
          this.cdr.markForCheck();
        },
        error: () => {
          this.mostrarNotificacion('❌ Error al guardar');
          this.cdr.markForCheck();
        }
      });
    }
  }

  seleccionarParaEditar(p: Pelicula): void {
    this.editando = true;
    this.nuevaPelicula = { ...p };
  }

  eliminarPelicula(id?: number): void {
    if (!id) return;
    if (confirm('¿Está seguro de eliminar esta película?')) {
      // DELETE (Reto 10): Borrado
      this.peliculaService.eliminar(id).subscribe({
        next: () => {
          this.peliculas = this.peliculas.filter(p => p.id !== id);
          this.mostrarNotificacion('🗑️ Película eliminada');
          this.cdr.markForCheck();
        },
        error: () => {
          console.error('Error al eliminar');
          this.cdr.markForCheck();
        }
      });
    }
  }

  limpiarFormulario(): void {
    this.editando = false;
    this.nuevaPelicula = {
      titulo: '',
      reseñaPersonal: '',
      genero: '',
      anio: 2024,
      sinopsis: '',
      imagenUrl: '',
      estado: 'PENDIENTE',
      calificacionPersonal: 5.0
    };
  }

  mostrarNotificacion(msg: string): void {
    this.mensajeNotificacion = msg;
    setTimeout(() => {
      this.mensajeNotificacion = '';
      this.cdr.markForCheck();
    }, 3000);
  }

  // --- Integración TMDB ---

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
        this.mostrarNotificacion('❌ Error al buscar en TMDB');
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