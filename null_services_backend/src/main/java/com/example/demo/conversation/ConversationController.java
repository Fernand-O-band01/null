package com.example.demo.conversation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PatchMapping;


import java.util.List;

/**
 * Controlador dedicado al manejo de endpoints
 * conversaciones (CREAR, GRUPO DE CONVERSACIÓN,
 * OBTENER CONVERSACIONES , OCULTAR CONVERSACIONES).
 */

@RestController
@RequestMapping("/conversations")
@RequiredArgsConstructor
public class ConversationController {

    /**
     * Servicio encargado de manejar la lógica para las conversaciones.
     */
    private final ConversationService conversationService;

    /**
     * ENDPOINT: POST /conversations/{targetUserId}
     * Propósito: Crea o recupera un chat de 1 vs 1.
     * * @param targetUserId El ID del amigo con el que
     * quieres hablar (viene en la URL).
     * @param connectedUser El usuario que está haciendo
     * la petición (obtenido del token JWT).
     * @param targetUserId Usuario seleccionado para crear
     * una conversación.
     * @return Los datos básicos de la conversación (ConversationResponse).
     */
    @PostMapping("/{targetUserId}")
    public ResponseEntity<ConversationResponse> createConversation(
            @PathVariable final Integer targetUserId,
            final Authentication connectedUser
    ) {
        return ResponseEntity.ok(
                conversationService.createOrGetConversation(
                        targetUserId, connectedUser
                )
        );
    }

    /**
     * ENDPOINT: POST /conversations/group
     * Propósito: Crea un chat grupal nuevo
     * con múltiples personas.
     * @param targetUserIds Una lista de IDs de
     * amigos enviada en el body (ej: [2, 5, 8]).
     * @param connectedUser El usuario que está creando
     * el grupo (obtenido del token JWT).
     * @return Los datos de la conversación grupal creada.
     */
    @PostMapping("/group")
    public ResponseEntity<ConversationResponse> createGroupConversation(
            @RequestBody final List<Integer> targetUserIds,
            final Authentication connectedUser
    ) {
        return ResponseEntity.ok(
                conversationService.createGroupConversation(
                        targetUserIds, connectedUser
                )
        );
    }

    /**
     * ENDPOINT: GET /conversations
     * Propósito: Obtiene la lista de todos
     * los chats (1v1 o grupales) a los que pertenece el usuario.
     * Esto alimenta tu barra lateral izquierda en
     * Angular (dm-sidebar).
     * @param connectedUser El usuario autenticado actual.
     * @return Una lista con todas sus conversaciones.
     */
    @GetMapping
    public ResponseEntity<List<ConversationResponse>> getConversation(
            final Authentication connectedUser
    ) {
        return ResponseEntity.ok(
                conversationService.getUserConversation(connectedUser));
    }

    /**
     * Endpoint para ocultar visualmente una conversación.
     * <p>
     * Recibe la petición para marcar una conversación
     * como oculta para el usuario actual.
     * NOTA DE ARQUITECTURA: Se utiliza el verbo
     * HTTP PATCH en lugar de DELETE, ya que
     * el recurso (la conversación) no se elimina del servidor,
     * sino que únicamente se
     * actualiza parcialmente su estado de visibilidad
     * mediante la relación 'hiddenBy'.
     * </p>
     *
     * @param conversationId El ID de la conversación que
     * se desea ocultar, obtenido de la URL.
     * @param connectedUser  Los datos de autenticación
     * del usuario actual provistos por Spring Security.
     * @return ResponseEntity con código 200 (OK)
     * sin contenido en el cuerpo.
     */
    @PatchMapping("/{conversationId}/hide")
    public ResponseEntity<Void> hideConversation(
            @PathVariable final Long conversationId,
            final Authentication connectedUser
    ) {
        conversationService.hideConversation(
                conversationId, connectedUser
        );
        return ResponseEntity.ok().build();
    }
}
