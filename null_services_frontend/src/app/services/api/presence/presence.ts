import { Injectable, inject } from '@angular/core';
import { Websocket } from '../websocket/websocket';// Ajusta la ruta a tu archivo
import { map, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PresenceService {

  private ws = inject(Websocket)

  /**
   * Escucha los cambios de estado de un usuario específico.
   * Usamos watch() de rxStomp, que devuelve un Observable.
   */
  watchUserStatus(userId: number): Observable<string> {
    return this.ws.rxStomp
      .watch(`/topic/user/status/${userId}`)
      .pipe(
        map(message => message.body) // Extraemos solo el string ("AWAY", "ONLINE", etc.)
      );
  }
}