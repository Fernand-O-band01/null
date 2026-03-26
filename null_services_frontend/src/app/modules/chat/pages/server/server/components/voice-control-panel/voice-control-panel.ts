import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-voice-control-panel',
  imports: [CommonModule],
  templateUrl: './voice-control-panel.html',
  styleUrl: './voice-control-panel.css',
})
export class VoiceControlPanel {

  @Input() channelName = '';
  @Input() serverName = '';
  showSmallPanel = false;
  @Output() toDisconnect = new EventEmitter<void>();

  disconnect() {
    this.toDisconnect.emit();
  }

}
