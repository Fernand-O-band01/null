import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { ConversationResponse } from '../model/conversationResponse';

@Injectable({
  providedIn: 'root'
})
export class ChatNavigationService {
  
  private openChatSource = new Subject<ConversationResponse>();
  

  public openChat$ = this.openChatSource.asObservable();


  openChat(conversationId: number, friendName: string): void {
    this.openChatSource.next({ id: conversationId, otherUserName: friendName });
  }
}