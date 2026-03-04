import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
// 🚀 Asegúrate de importar AuthenticationResponse desde tu ruta de la API
import { AuthenticationResponse } from '../model/authenticationResponse';

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  
  // 1. Buscamos el objeto completo en localStorage (si existe)
  private getInitialUser(): AuthenticationResponse | null {
    const storedUser = localStorage.getItem('currentUser');
    return storedUser ? JSON.parse(storedUser) : null;
  }

  // 2. El Subject ahora guarda el objeto AuthenticationResponse
  private currentUserSubject = new BehaviorSubject<AuthenticationResponse | null>(this.getInitialUser());
  
  // 3. Este es el observable al que se van a suscribir tu panel y tu modal
  public currentUser$ = this.currentUserSubject.asObservable();

  // 4. (Opcional) Mantenemos nickname$ por si lo usas en otros componentes y no quieres romperlos hoy
  public nickname$ = new BehaviorSubject<string>(this.getInitialUser()?.nickname || 'Usuario').asObservable();

  constructor() {}

  // 🚀 Reemplaza a setNickname: Guarda TODO el objeto (Se llama desde el Login)
  setCurrentUser(user: AuthenticationResponse) {
    localStorage.setItem('currentUser', JSON.stringify(user));
    this.currentUserSubject.next(user);
  }

  // 🚀 NUEVO: Actualiza solo el estado y notifica a todos (Se llama desde el user-panel)
  updateLocalStatus(status: AuthenticationResponse.StatusEnum) {
    const current = this.currentUserSubject.value;
    if (current) {
      const updatedUser = { ...current, status: status };
      this.setCurrentUser(updatedUser); // Re-guarda y emite el cambio
    }
  }

  // 🚀 Limpia todo al cerrar sesión
  clearSesion() {
    localStorage.removeItem('token');
    localStorage.removeItem('currentUser');
    this.currentUserSubject.next(null);
  }

}