import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NavbarComponent } from './components/navbar/navbar.component';
import { NotificacionBannerComponent } from './components/notificacion-banner/notificacion-banner.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, NavbarComponent, NotificacionBannerComponent],
  templateUrl: './app.html'
})
export class AppComponent {}