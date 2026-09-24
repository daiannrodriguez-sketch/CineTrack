import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NotificacionService } from '../../services/notificacion.service';

@Component({
  selector: 'app-notificacion-banner',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './notificacion-banner.component.html'
})
export class NotificacionBannerComponent {
  notificacionService = inject(NotificacionService);
}