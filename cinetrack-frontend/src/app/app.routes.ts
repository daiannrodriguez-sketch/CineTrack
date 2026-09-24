import { Routes } from '@angular/router';
import { PeliculaListComponent } from './components/pelicula-list/pelicula-list.component';
import { PeliculaFormComponent } from './components/pelicula-form/pelicula-form.component';
import { PeliculaDetalleComponent } from './components/pelicula-detalle/pelicula-detalle.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';

export const routes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'peliculas', component: PeliculaListComponent },
  { path: 'peliculas/nueva', component: PeliculaFormComponent },
  { path: 'peliculas/:id/editar', component: PeliculaFormComponent },
  { path: 'peliculas/:id', component: PeliculaDetalleComponent }
];