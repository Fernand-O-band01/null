import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

export interface ActiveVoiceParticipant {
  identity: string;
  isSpeaking: boolean;
  isLocal: boolean;
}

@Component({
  selector: 'app-voice-room',
  imports: [CommonModule],
  templateUrl: './voice-room.html',
  styleUrl: './voice-room.css',
})
export class VoiceRoom {

  // 👥 Recibe la lista de los que están en la llamada
  @Input({ required: true }) participants: ActiveVoiceParticipant[] = []; 
  // 🏷️ Recibe el nombre del canal para mostrarlo arriba
  @Input({ required: true }) channelName = '';

  @Input ({required: true}) serverName = '';

}
