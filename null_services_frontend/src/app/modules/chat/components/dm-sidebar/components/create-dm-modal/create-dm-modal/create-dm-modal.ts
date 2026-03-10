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
    
    // 💡 Como tu método actual (createConversation) solo acepta 1 ID a la vez, 
    // limpiamos el set para que solo se pueda seleccionar de a una persona por ahora.
    this.selectedFriendIds.clear();
    this.selectedFriendIds.add(friendId);
  }

  isFriendSelected(friendId: number | undefined): boolean {
    if (friendId === undefined) return false;
    return this.selectedFriendIds.has(friendId);
  }

  createDirectMessage(): void {
  if (this.selectedFriendIds.size === 0) return;

  const friendId = Array.from(this.selectedFriendIds)[0];
  const friendData = this.friendsList.find(f => f.id === friendId);
  
  console.log('Creando DM con el ID:', friendId);

  this.conversationControllerService.createConversation(friendId as any).subscribe({
    next: (response: any) => {
      
      // 1. Cerramos el modal
      this.closeModal();
      
      // 2. 🚀 ¡USAMOS EL WALKIE-TALKIE!
      // Le avisamos a toda la app que queremos abrir este chat específico
      const name = friendData?.name || 'Usuario';
      this.chatNavigationService.openChat(response.id, name);
      
      console.log("¡Chat creado/obtenido con éxito y señal emitida!", response);
    },
    error: (err) => console.error('Error al crear/abrir DM chat', err)
  });
}

  closeModal(): void {
    this.modalService.closeCreateDm(); 
  }
}