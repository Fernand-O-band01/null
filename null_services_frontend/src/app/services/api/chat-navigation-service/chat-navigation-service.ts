import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { ConversationResponse } from '../model/conversationResponse';

@Injectable({
  providedIn: 'root'
})
export class ChatNavigationService {
  
  // Usamos un Subject porque no necesitamos recordar el valor anterior, solo emitir un evento en el momento.
  private openChatSource = new Subject<ConversationResponse>();
  
  // El canal de radio que tu componente principal va a escuchar
  public openChat$ = this.openChatSource.asObservable();

  // La función que usará el Modal para gritar "¡Abran este chat!"
  openChat(conversationId: number, friendName: string): void {
    this.openChatSource.next({ id: conversationId, otherUserName: friendName });
  }
}