import { Component, OnInit, OnDestroy, ChangeDetectorRef, inject } from '@angular/core'; 
import { CommonModule } from '@angular/common';
import { Subscription } from 'rxjs';

import { DmSidebar } from "../../../components/dm-sidebar/dm-sidebar";
import { FriendsOnline } from '../components/friends-online/friends-online';
import { FriendsAll } from '../components/friends-all/friends-all/friends-all';
import { FriendsPending } from '../components/friends-pending/friends-pending/friends-pending';
import { FriendAdd } from '../components/friends-add/friend-add/friend-add';
import { ChatRoom } from '../components/chat-room/chat-room/chat-room';

import { Modalservice } from '../../../../../services/api/modalservice/modalservice';

import { ChatNavigationService } from '../../../../../services/api/chat-navigation-service/chat-navigation-service';
import { ConversationResponse } from '../../../../../services/api';

type tabType = 'ONLINE' | 'ALL' | 'PENDING' | 'ADD';
type ViewType = 'FRIENDS' | 'CHAT';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule, 
    DmSidebar, 
    FriendsOnline, 
    FriendsAll, 
    FriendsPending,
    FriendAdd,
    ChatRoom,
  ],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements OnInit, OnDestroy {

  currentView: ViewType = 'FRIENDS';
  activeTab: tabType = 'ALL';

  activeConversationId: number | null = null;
  activeFriendName = '';

  private chatNavSub?: Subscription;

  private chatNavigationService = inject(ChatNavigationService)
  private modalService = inject(Modalservice)
  private cdr = inject(ChangeDetectorRef) 


  ngOnInit() {
    this.chatNavSub = this.chatNavigationService.openChat$.subscribe(data => {
      console.log('📻 home.ts escuchó el modal! Abriendo chat:', data);
      
      this.openChat(data);
      this.cdr.detectChanges(); 
    });
  }

  ngOnDestroy() {
    this.chatNavSub?.unsubscribe();
  }

  openChat(eventData: ConversationResponse) {
    console.log('Solicitud para abrir chat:', eventData);

    this.activeConversationId = eventData.id ?? null;
    this.activeFriendName = eventData.otherUserName ?? '';
   


    // Cambiamos la vista
    this.currentView = 'CHAT';
  }

  setTab(tab: tabType) {
    this.activeTab = tab;
  }

  openCreateDMModal(): void{
    this.modalService.openCreateDm();
  }

  openFriendsList() {
    this.currentView = 'FRIENDS';
    this.activeConversationId = null;
    this.activeFriendName = '';
  }
}