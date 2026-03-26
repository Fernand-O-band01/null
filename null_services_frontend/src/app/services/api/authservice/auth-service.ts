import { Injectable, inject } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { AuthenticationResponse } from '../model/authenticationResponse';
import { Token } from '../token/token'; 

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private getInitialUser(): AuthenticationResponse | null {
    const storedUser = localStorage.getItem('currentUser');
    return storedUser ? JSON.parse(storedUser) : null;
  }

  private currentUserSubject = new BehaviorSubject<AuthenticationResponse | null>(this.getInitialUser());
  public currentUser$ = this.currentUserSubject.asObservable();
  public nickname$ = new BehaviorSubject<string>(this.getInitialUser()?.nickname || 'Usuario').asObservable();

  private tokenService = inject(Token)

  public get currentUserValue(): AuthenticationResponse | null {
    return this.currentUserSubject.value;
  }


  public getMyUserId(): number {

    const user = this.currentUserValue;
    if (user && (user as AuthenticationResponse & {id?: number}).id) {
      return (user as AuthenticationResponse & {id: number}).id;
    }


    const tokenStr = this.tokenService.token;
    if (tokenStr) {
      try {
        const payload = JSON.parse(atob(tokenStr.split('.')[1]));
        return payload.userId || payload.id || 0; 
      } catch (e) {
        console.error('Error decodificando token en AuthService', e);
      }
    }
    return 0; 
  }

  setCurrentUser(user: AuthenticationResponse) {
    localStorage.setItem('currentUser', JSON.stringify(user));
    this.currentUserSubject.next(user);
  }

  updateLocalStatus(status: AuthenticationResponse.StatusEnum) {
    const current = this.currentUserSubject.value;
    if (current) {
      const updatedUser = { ...current, status: status };
      this.setCurrentUser(updatedUser);
    }
  }

  clearSesion() {
    localStorage.removeItem('token');
    localStorage.removeItem('currentUser');
    this.currentUserSubject.next(null);
  }
}