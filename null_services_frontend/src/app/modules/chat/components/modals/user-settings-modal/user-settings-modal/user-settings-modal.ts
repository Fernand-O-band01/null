import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Modalservice } from '../../../../../../services/api/modalservice/modalservice';

@Component({
  selector: 'app-user-settings-modal',
  imports: [CommonModule],
  templateUrl: './user-settings-modal.html',
  styleUrl: './user-settings-modal.css',
})
export class UserSettingsModal {

  constructor(
    private modalService: Modalservice
  ){}

  closeModal(): void {
    this.modalService.closeSettings();
  }

}
