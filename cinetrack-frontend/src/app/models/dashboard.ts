export interface UltimaOperacion {
  peliculaTitulo: string;
  calificacion: number;
  fecha: string;
  descripcion: string;
}

export interface DashboardStats {
  totalPeliculas: number;
  vistas: number;
  pendientes: number;
  viendo: number;
  alertas: number;
  ultimasOperaciones: UltimaOperacion[];
}