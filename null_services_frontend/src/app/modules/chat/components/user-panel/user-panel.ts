import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { AuthService } from '../../../../services/api/authservice/auth-service';

import { Websocket } from '../../../../services/api/websocket/websocket';

import { UserPanelPopUp } from './user-panel-pop-up/user-panel-pop-up/user-panel-pop-up';
import { AuthenticationResponse } from '../../../../services/api';
import { UsersService } from '../../../../services/api';


@Component({
  selector: 'app-user-panel',
  imports: [CommonModule, UserPanelPopUp],
  templateUrl: './user-panel.html',
  styleUrl: './user-panel.css',
  host: {
    'class': 'contents'
  }
})
export class UserPanel implements OnInit, OnDestroy {

  username: string = 'Usuario';
  private sub!: Subscription;

  isStatusMenuOpen: boolean = false;
  currentStatus: AuthenticationResponse.StatusEnum = AuthenticationResponse.StatusEnum.Online;

  constructor(
    private router: Router,
    private nickService: AuthService,
    private userService: UsersService,
    private ws: Websocket
  ){}

  ngOnInit(): void {
    this.sub = this.nickService.nickname$.subscribe(nickname => {
      this.username = nickname;
    });

    const savedStatus = localStorage.getItem('userStatus');
    if(savedStatus){
      this.currentStatus = savedStatus as AuthenticationResponse.StatusEnum
    }
  }

  ngOnDestroy() {
    if(this.sub) {
      this.sub.unsubscribe();
    }
  }
  
  toggleStatusMenu() {
    this.isStatusMenuOpen = !this.isStatusMenuOpen;
  }

  onStatusChange(newStatus: AuthenticationResponse.StatusEnum) {
  this.isStatusMenuOpen = false; 

  const previousStatus = this.currentStatus;
  this.currentStatus = newStatus;

  localStorage.setItem('userStatus', newStatus)

  // Llamamos al servicio real que me acabas de mostrar
  this.userService.updateStatus(newStatus as any).subscribe({
    next: () => console.log(`Estado guardado en BD: ${newStatus}`),
    error: (err) => {
      console.error('Error al guardar el estado', err);
      this.currentStatus = previousStatus;
    }
  });
}


  logout(){
    console.log('Cerrando sesion...');

    if (this.ws.rxStomp.active) {
      this.ws.rxStomp.deactivate();
    }

    localStorage.removeItem('token');
    localStorage.removeItem('userStatus')
    this.nickService.clearSesion();
    this.router.navigate(['/login']);
  }

}

