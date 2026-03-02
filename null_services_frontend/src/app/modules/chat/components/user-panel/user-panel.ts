import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';

import { AuthService } from '../../../../services/api/authservice/auth-service';
import { Websocket } from '../../../../services/api/websocket/websocket';
import { AuthenticationResponse, UsersService } from '../../../../services/api';
import { Modalservice } from '../../../../services/api/modalservice/modalservice';
import { UserPanelPopUp } from './user-panel-pop-up/user-panel-pop-up/user-panel-pop-up';

@Component({
  selector: 'app-user-panel',
  standalone: true, // ¡Ojo! Añadí standalone: true por si acaso, verifica si lo necesitas
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

  // 🚀 TUS VARIABLES ORIGINALES RESTAURADAS
  isStatusMenuOpen: boolean = false;
  currentStatus: AuthenticationResponse.StatusEnum = AuthenticationResponse.StatusEnum.Online;

  // 🚀 LA NUEVA VARIABLE PARA EL ENGRANAJE
  showSmallPanel: boolean = false;

  constructor(
    private router: Router,
    private nickService: AuthService,
    private userService: UsersService,
    private ws: Websocket,
    private modalService: Modalservice
  ) {}

  ngOnInit(): void {
    this.sub = this.nickService.nickname$.subscribe(nickname => {
      this.username = nickname;
    });

    const savedStatus = localStorage.getItem('userStatus');
    if (savedStatus) {
      this.currentStatus = savedStatus as AuthenticationResponse.StatusEnum;
    }
  }

  ngOnDestroy(): void {
    if (this.sub) {
      this.sub.unsubscribe();
    }
  }
  
  // 🚀 TU FUNCIÓN ORIGINAL RESTAURADA (Y conectada al engranaje)
  toggleStatusMenu(event?: Event): void {
    if (event) {
      event.stopPropagation();
    }
    this.isStatusMenuOpen = !this.isStatusMenuOpen;
    
    // Sincronizamos la variable del engranaje para que se bloquee
    this.showSmallPanel = this.isStatusMenuOpen; 
  }

  openSettingsModal(): void {
    // El candado de seguridad
    if (this.showSmallPanel || this.isStatusMenuOpen) {
      return; 
    }
    this.modalService.openSettings();
  }

  onStatusChange(newStatus: AuthenticationResponse.StatusEnum): void {
    this.isStatusMenuOpen = false; 
    this.showSmallPanel = false; // Liberamos el engranaje

    const previousStatus = this.currentStatus;
    this.currentStatus = newStatus;

    localStorage.setItem('userStatus', newStatus);

    this.userService.updateStatus(newStatus as any).subscribe({
      next: () => console.log(`Estado guardado en BD: ${newStatus}`),
      error: (err) => {
        console.error('Error al guardar el estado', err);
        this.currentStatus = previousStatus;
      }
    });
  }

  logout(): void {
    console.log('Cerrando sesion...');

    if (this.ws.rxStomp.active) {
      this.ws.rxStomp.deactivate();
    }

    localStorage.removeItem('token');
    localStorage.removeItem('userStatus');
    this.nickService.clearSesion();
    this.router.navigate(['/login']);
  }
}