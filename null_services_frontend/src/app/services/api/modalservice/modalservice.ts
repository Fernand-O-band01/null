import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class Modalservice {
  
  // Variable reactiva que guarda si el panel de ajustes está abierto o cerrado
  private showSettingsSource = new BehaviorSubject<boolean>(false);
  
  // El observable que los componentes van a escuchar
  public showSettings$ = this.showSettingsSource.asObservable();

  // Función para abrir
  openSettings(): void {
    this.showSettingsSource.next(true);
  }

  // Función para cerrar
  closeSettings(): void {
    this.showSettingsSource.next(false);
  }
}