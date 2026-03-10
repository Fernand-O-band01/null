import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';

// 🚀 IMPORTACIONES CORRECTAS DE TU API
import { FriendResponseDTO, ConversationControllerService } from '../../../../../../../services/api';
import { FriendsDataService } from '../../../../../../../services/api/friends-data-service/friends-data-service';
import { Modalservice } from '../../../../../../../services/api/modalservice/modalservice';

import { ChatNavigationService } from '../../../../../../../services/api/chat-navigation-service/chat-navigation-service';

@Component({
  selector: 'app-create-dm-modal',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './create-dm-modal.html',
  // Si estás usando 100% Tailwind en el HTML, puedes quitar el styleUrl si el archivo .css está vacío
})
export class CreateDmModalComponent implements OnInit, OnDestroy {
  
  // Estado
  friendsList: FriendResponseDTO[] = [];
  selectedFriendIds = new Set<number>();
  searchTerm: string = '';
  private sub?: Subscription;

  constructor(
    private friendsDataService: FriendsDataService,
    private modalService: Modalservice,
    private conversationControllerService: ConversationControllerService, // 🚀 AHORA SÍ ES TU SERVICIO REAL
    private router: Router,
    private chatNavigationService: ChatNavigationService
  ) {}

  ngOnInit(): void {
    // 1. Nos suscribimos a la fuente de datos compartida
    this.sub = this.friendsDataService.allFriends$.subscribe(friends => {
      this.friendsList = friends;
    });
  }

  ngOnDestroy(): void {
    this.sub?.unsubscribe();
  }

  get filteredFriends(): FriendResponseDTO[] {
    if (!this.searchTerm.trim()) return this.friendsList;
    
    const searchLower = this.searchTerm.toLowerCase();
    return this.friendsList.filter(f => 
      f.name?.toLowerCase().includes(searchLower)
    );
  }

  toggleSelection(friendId: number | undefined): void {
    if (friendId === undefined) return;
    
    
    if (this.selectedFriendIds.has(friendId)) {
      this.selectedFriendIds.delete(friendId);
    } else {
      this.selectedFriendIds.add(friendId);
    }
  }

  isFriendSelected(friendId: number | undefined): boolean {
    if (friendId === undefined) return false;
    return this.selectedFriendIds.has(friendId);
  }

  createDirectMessage(): void {
    if (this.selectedFriendIds.size === 0) return;

    // Convertimos el Set a un Array de IDs
    const recipientIds = Array.from(this.selectedFriendIds);

    // ==========================================
    // 👤 CASO 1: CHAT 1 VS 1
    // ==========================================
    if (recipientIds.length === 1) {
      const friendId = recipientIds[0];
      const friendData = this.friendsList.find(f => f.id === friendId);
      
      console.log('Creando DM 1v1 con el ID:', friendId);

      this.conversationControllerService.createConversation(friendId as any).subscribe({
        next: (response: any) => {
          this.closeModal();
          const name = friendData?.name || response.otherUserName || 'Usuario';
          this.chatNavigationService.openChat(response.id, name);
        },
        error: (err) => console.error('Error al crear DM 1v1', err)
      });
    } 
    // ==========================================
    // 👥 CASO 2: CHAT GRUPAL
    // ==========================================
    else {
      console.log('Creando DM Grupal con los IDs:', recipientIds);

      // 🚀 Usamos tu NUEVO método generado por OpenAPI
      this.conversationControllerService.createGroupConversation(recipientIds).subscribe({
        next: (response: any) => {
          this.closeModal();
          
          // El backend ya nos devuelve el nombre unido (ej: "Juan, María") en response.otherUserName
          const groupName = response.otherUserName || 'Grupo Nuevo';
          
          this.chatNavigationService.openChat(response.id, groupName);
        },
        error: (err) => console.error('Error al crear DM Grupal', err)
      });
    }
  }

  closeModal(): void {
    this.modalService.closeCreateDm(); 
  }
}