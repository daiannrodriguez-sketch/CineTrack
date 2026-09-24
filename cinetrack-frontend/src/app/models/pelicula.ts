export interface Pelicula {
  id?: number;
  titulo: string;
  resenaPersonal: string;
  genero: string;
  anio: number;
  sinopsis: string;
  imagenUrl: string;
  estado: 'PENDIENTE' | 'VIENDO' | 'VISTA';
  calificacionPersonal: number;
}