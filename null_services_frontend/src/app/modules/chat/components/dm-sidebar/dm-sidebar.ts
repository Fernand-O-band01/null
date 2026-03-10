import { Component, OnInit, OnDestroy, Output, EventEmitter, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Subscription } from 'rxjs'; // 🚀 Para gestionar la memoria

import { ConversationControllerService } from '../../../../services/api';
import { ConversationResponse } from '../../../../services/api';
import { Modalservice } from '../../../../services/api/modalservice/modalservice';
import { CreateDmModalComponent } from './components/create-dm-modal/create-dm-modal/create-dm-modal';

// 📻 Importamos el servicio de navegación que une el Modal con el resto de la App
import { ChatNavigationService } from '../../../../services/api/chat-navigation-service/chat-navigation-service'; 

@Component({
  selector: 'app-dm-sidebar',
  standalone: true,
  imports: [CommonModule, CreateDmModalComponent],
  templateUrl: './dm-sidebar.html',
  styleUrl: './dm-sidebar.css',
})
export class DmSidebar implements OnInit, OnDestroy {

  // Lista de conversaciones que se pintan en el HTML
  conversations: ConversationResponse[] = [];
  
  // Guardamos la suscripción para poder cancelarla al cerrar la app (evita fugas de memoria)
  private navSub?: Subscription;

  // Evento para avisar al componente Home que seleccionamos un chat de la lista
  @Output() onConversationSelect = new EventEmitter<ConversationResponse>();

  constructor(
    private conversationService: ConversationControllerService,
    private cdr: ChangeDetectorRef,
    public modalService: Modalservice,
    private chatNavigationService: ChatNavigationService // 🚀 Inyectamos el "Walkie-Talkie"
  ){}

  /**
   * Se ejecuta al arrancar el componente lateral.
   */
  ngOnInit(): void {
      // 1. Cargamos las conversaciones que ya existen en la base de datos
      this.loadConversation();
      
      // 2. 📻 NOS PONEMOS A ESCUCHAR EL CANAL DE RADIO
      // Este bloque se ejecutará CADA VEZ que el modal cree un chat exitosamente.
      this.navSub = this.chatNavigationService.openChat$.subscribe(data => {
        
        // Verificamos si la conversación que acaba de nacer ya está en nuestra lista
        const alreadyExists = this.conversations.find(c => c.id === data.conversationId);
        
        if (!alreadyExists) {
          console.log('✨ Sidebar detectó nuevo chat: Agregando a la lista sin recargar...');

          // Agregamos el nuevo chat al PRINCIPIO de la lista (unshift)
          // Usamos la data que viene del "Walkie-Talkie" { conversationId, friendName }
          this.conversations.unshift({
            id: data.conversationId,
            otherUserName: data.friendName
          });
          
          // 🚀 EL PASO CRUCIAL:
          // Como este cambio ocurre dentro de una suscripción (fuera del ciclo normal de Angular),
          // obligamos a la barra lateral a redibujarse para mostrar el nuevo chat.
          this.cdr.detectChanges(); 
        }
      });
  }

  /**
   * Importante: Cuando el componente se destruye, apagamos el radio.
   */
  ngOnDestroy(): void {
      if (this.navSub) {
        this.navSub.unsubscribe();
      }
  }

  /**
   * Carga inicial desde el servidor.
   */
  loadConversation(){
    this.conversationService.getConversation().subscribe({
      next: (data) => {
        // Asignamos la lista que viene del servidor (Spring Boot)
        this.conversations = data;
        this.cdr.detectChanges(); // Aseguramos que se pinten los chats cargados
      },
      error: (err) => console.error("No se pudieron cargar las salas de chat", err)
    });
  }

  /**
   * Se ejecuta al hacer clic EXACTAMENTE en la "X" que aparece al hacer hover
   */
  removeConversation(event: Event, conv: ConversationResponse) {
    // 1. ESCUDO: Evita que el clic "traspase" la X y active el selectConversation() del fondo
    event.stopPropagation(); 

    console.log('Solicitud para quitar chat de la barra:', conv);

    // 2. UX MÁGICA: Lo borramos de la vista instantáneamente
    // Filtramos la lista para quedarnos con todos MENOS el que acabamos de clickear
    this.conversations = this.conversations.filter(c => c.id !== conv.id);

    // 3. LÓGICA DE NEGOCIO (El puente hacia tu Backend)
    // Discord maneja diferente borrar un 1v1 que abandonar un Grupo.
    const isGroup = conv.otherUserName?.includes(',');

    if (isGroup) {
      console.log('Es un grupo: Se debería llamar a la API para ABANDONAR o ELIMINAR el grupo.');
      // AQUÍ IRÁ TU LLAMADA AL BACKEND:
      // this.conversationService.leaveGroup(conv.id).subscribe(...)
    } else {
      console.log('Es 1v1: Se debería llamar a la API para OCULTAR este chat de tu vista.');
      // AQUÍ IRÁ TU LLAMADA AL BACKEND:
      // this.conversationService.hideConversation(conv.id).subscribe(...)
    }
  }

  /**
   * Al hacer clic en un chat de la lista, avisamos al padre (Home).
   */
  selectConversation(conv: ConversationResponse){
    this.onConversationSelect.emit(conv);
  }

  /**
   * Abre el modal de creación (el + de la sidebar).
   */
  openCreateDMModal(): void{
    this.modalService.openCreateDm();
  }
}