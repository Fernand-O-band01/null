package com.example.demo.message;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador REST encargado de gestionar
 * las operaciones relacionadas con los mensajes.
 * Proporciona los endpoints para enviar
 * nuevos mensajes y consultar el historial
 * tanto de chats privados como de canales públicos.
 */
@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    /**
     * Servicio que contiene la lógica de negocio
     * para el procesamiento y almacenamiento de mensajes.
     */
    private final MessageService messageService;

    /**
     * Guarda un nuevo mensaje en la base de datos de MongoDB.
     * La lógica interna decide si el mensaje pertenece
     * a un chat privado o a un canal de servidor
     * basándose en los identificadores proporcionados en
     * el cuerpo de la petición.
     *
     * @param message El objeto de mensaje con los datos
     * a guardar (contenido, remitente, destino).
     * @return Una respuesta HTTP 200 (OK) con el
     * mensaje recién guardado y su ID generado.
     */
    @PostMapping
    public ResponseEntity<Message> sendMessage(
            final @RequestBody Message message) {
        return ResponseEntity.ok(messageService
                .saveMessage(message));
    }

    /**
     * Recupera el historial completo de mensajes
     * de una conversación privada (Mensaje Directo).
     *
     * @param conversationId El identificador único de la
     * conversación privada.
     * @return Una respuesta HTTP 200 (OK) con la lista de
     * mensajes históricos de ese chat.
     */
    @GetMapping("/chat/{conversationId}")
    public ResponseEntity<List<Message>> getChatHistory(
            final @PathVariable Long conversationId) {
        return ResponseEntity.ok(messageService
                .findChatMessages(conversationId));
    }

    /**
     * Recupera el historial de mensajes de
     * un canal específico dentro de un servidor.
     * Este es el endpoint que el cliente (Angular)
     * llama al entrar a la vista de un canal.
     *
     * @param channelId El identificador único del canal de servidor.
     * @return Una respuesta HTTP 200 (OK) con
     * la lista de mensajes históricos de ese canal.
     */
    @GetMapping("/channel/{channelId}")
    public ResponseEntity<List<Message>> getChannelHistory(
            final @PathVariable Long channelId) {
        return ResponseEntity.ok(messageService
                .findChannelMessages(channelId));
    }

}
