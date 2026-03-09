import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class Modalservice {
  
  // ==========================================
  // ⚙️ MODAL DE AJUSTES (SETTINGS)
  // ==========================================
  private showSettingsSource = new BehaviorSubject<boolean>(false);
  public showSettings$ = this.showSettingsSource.asObservable();

  openSettings(): void {
    this.showSettingsSource.next(true);
  }

  closeSettings(): void {
    this.showSettingsSource.next(false);
  }

  // ==========================================
  // 💬 🚀 NUEVO: MODAL DE CREAR MENSAJE DIRECTO
  // ==========================================
  private showCreateDmSource = new BehaviorSubject<boolean>(false);
  public showCreateDm$ = this.showCreateDmSource.asObservable();

  openCreateDm(): void {
    this.showCreateDmSource.next(true);
  }

  closeCreateDm(): void {
    this.showCreateDmSource.next(false);
  }

  // ==========================================
  // 🧹 UTILIDAD: CERRAR TODOS LOS MODALES
  // ==========================================
  closeAllModals(): void {
    this.showSettingsSource.next(false);
    this.showCreateDmSource.next(false);
  }
}