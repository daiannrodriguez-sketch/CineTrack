import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PeliculaService } from './services/pelicula.services';
import { Pelicula } from './models/pelicula';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.html'
})
export class AppComponent implements OnInit {
  peliculaService = inject(PeliculaService);
  peliculas: Pelicula[] = [];
  editando: boolean = false;
  mensajeNotificacion: string = '';

  nuevaPelicula: Pelicula = {
    titulo: '',
    director: '',
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
      next: (datos: Pelicula[]) => this.peliculas = datos,
      error: (err) => console.error('Error conectando con Spring Boot:', err)
    });
  }

  guardarPelicula(): void {
    if (this.editando && this.nuevaPelicula.id) {
      // PUT (Reto 9): Actualización ultra-rápida local
      this.peliculaService.actualizar(this.nuevaPelicula.id, this.nuevaPelicula).subscribe({
        next: (res) => {
          const index = this.peliculas.findIndex(p => p.id === res.id);
          if (index !== -1) this.peliculas[index] = res; // Modifica la tarjeta de inmediato
          this.mostrarNotificacion('✨ Película actualizada al instante');
          this.limpiarFormulario();
        },
        error: () => this.mostrarNotificacion('❌ Error al actualizar')
      });
    } else {
      // POST (Reto 8): Creación ultra-rápida local
      this.peliculaService.crear(this.nuevaPelicula).subscribe({
        next: (res) => {
          this.peliculas.push(res); // Inserta la tarjeta al instante
          this.mostrarNotificacion('✅ Película registrada con éxito');
          this.limpiarFormulario();
        },
        error: () => this.mostrarNotificacion('❌ Error al guardar')
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
      // DELETE (Reto 10): Borrado ultra-rápido local
      this.peliculaService.eliminar(id).subscribe({
        next: () => {
          this.peliculas = this.peliculas.filter(p => p.id !== id); // Remueve la tarjeta al instante
          this.mostrarNotificacion('🗑️ Película eliminada');
        },
        error: () => console.error('Error al eliminar')
      });
    }
  }

  limpiarFormulario(): void {
    this.editando = false;
    this.nuevaPelicula = {
      titulo: '',
      director: '',
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
    setTimeout(() => this.mensajeNotificacion = '', 3000);
  }
}