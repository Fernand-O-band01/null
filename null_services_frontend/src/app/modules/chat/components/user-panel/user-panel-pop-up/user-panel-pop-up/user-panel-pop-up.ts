import { Component, EventEmitter, Output, Input } from '@angular/core';
import { AuthenticationResponse } from '../../../../../../services/api';



@Component({
  selector: 'app-user-panel-pop-up',
  imports: [],
  templateUrl: './user-panel-pop-up.html',
  styleUrl: './user-panel-pop-up.css',
})
export class UserPanelPopUp {
  @Input() username: String = 'Usuario'
  @Input() currentStatus!: AuthenticationResponse.StatusEnum;
  @Output() statusSelected = new EventEmitter<AuthenticationResponse.StatusEnum>();

  public statusEnum = AuthenticationResponse.StatusEnum

  showSubMenu: boolean = false;

  toggleSubMenu(event: Event){
    event.stopPropagation();
    this.showSubMenu = !this.showSubMenu
  }

  selectStatus(status: AuthenticationResponse.StatusEnum, event: Event){
    event?.stopPropagation();
    this.statusSelected.emit(status)
    this.showSubMenu = false;
  }

}
