export interface Resena {
  id?: number;
  peliculaId: number;
  fecha: string;        // formato 'YYYY-MM-DD'
  calificacion: number;
  descripcion: string;
}