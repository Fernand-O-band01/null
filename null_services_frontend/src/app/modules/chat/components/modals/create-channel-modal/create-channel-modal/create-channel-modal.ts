import { Component, EventEmitter, Input, Output, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { ChannelRequest, ChannelResponse } from '../../../../../../services/api';

import { ServerControllerService} from '../../../../../../services/api';

@Component({
  selector: 'app-create-channel-modal',
  imports: [CommonModule, FormsModule],
  templateUrl: './create-channel-modal.html',
  styleUrl: './create-channel-modal.css',
})
export class CreateChannelModal {

  @Input({required: true}) serverId!: number;
  @Output() closePopUp = new EventEmitter<void>();
  @Output() channelCreated = new EventEmitter<ChannelResponse>();

  newChannelName = '';
  newChannelType: 'TEXT' | 'VOICE' = 'TEXT';
  isPrivateChannel = false;

  private serverService = inject(ServerControllerService)

  closeModal(): void{
    this.closePopUp.emit()
  }

  createChannel(): void {
    if (!this.newChannelName.trim() || !this.serverId) return;

    // Formateamos el nombre estilo Discord: "Mi Canal" -> "mi-canal"
    const formattedName = this.newChannelName.trim().toLowerCase().replace(/\s+/g, '-');

    // Construimos el DTO según la estructura de tu Backend
    const request: ChannelRequest = {
      name: formattedName,
      type: this.newChannelType,
      isPrivate: this.isPrivateChannel // El nombre debe coincidir con el ChannelRequest de Java
    };

    // Llamada directa al Backend
    this.serverService.createChannel(this.serverId, request).subscribe({
      next: (newChannel: ChannelResponse) => {
        // 🚀 ¡Éxito! Le enviamos el canal recién horneado al componente padre (Server)
        this.channelCreated.emit(newChannel);
      },
      error: (err) => {
        console.error('Error al crear el canal en la base de datos:', err);
      }
    });
  }
}
