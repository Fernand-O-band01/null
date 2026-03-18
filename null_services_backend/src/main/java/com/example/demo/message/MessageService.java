package com.example.demo.message;

import com.example.demo.conversation.ConversationRepository;
import com.example.demo.kafka.MessageProducer;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio que contiene la lógica de negocio
 * principal para la gestión de mensajes.
 * Se encarga de validar el destino, enriquecer
 * los mensajes con los datos del usuario,
 * persistirlos en MongoDB y publicarlos en Kafka
 * para su difusión en tiempo real.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    /** Repositorio para el almacenamiento
     * y consulta de mensajes en MongoDB. */
    private final MessageRepository messageRepository;

    /** Repositorio JPA para validar la existencia
     * de conversaciones (chats privados). */
    private final ConversationRepository conversationRepository;

    /** Productor de Kafka encargado de difundir
     * los mensajes guardados al broker. */
    private final MessageProducer messageProducer;

    /** Repositorio JPA para obtener la información
     * del usuario emisor (como su nickname). */
    private final UserRepository userRepository;

    /**
     * Guarda y difunde un nuevo mensaje
     * enriquecido con el apodo (nickname) del autor.
     * Valida que el destino (conversación privada o canal)
     * sea correcto antes de guardarlo.
     *
     * @param message El objeto del mensaje proveniente
     * del cliente con el contenido y destino.
     * @return El mensaje persistido en MongoDB, enriquecido
     * con la fecha actual y el nickname.
     */
    public Message saveMessage(
            final Message message) {

        // Validaciones de Destino (Guardias de Tráfico)
        if (message.getConversationId() != null) {
            if (!conversationRepository.existsById(
                    message.getConversationId())) {
                throw new RuntimeException(
                        "Error: La conversación de MD no existe.");
            }
        } else if (message.getChannelId() == null) {
            throw new IllegalArgumentException(
                    "El mensaje debe tener un channelId o un conversationId.");
        }

        // ENRIQUECIMIENTO (Aquí usamos tu UserRepository JPA)
        // Buscamos al autor por su sendId (Integer)
        String nickname = userRepository.findById(message.getSendId())
                .map(User::getNickName)
                .orElse("Usuario " + message.getSendId());

        // Seteamos los metadatos necesarios
        message.setSenderNickname(nickname);
        message.setTimestamp(LocalDateTime.now());

        // Persistencia en MongoDB
        Message savedMessage = messageRepository.save(message);

        // Difusión en tiempo real vía Kafka
        messageProducer.sendMessage(savedMessage);

        log.info("Mensaje de {} enviado con éxito al destino", nickname);

        return savedMessage;
    }

    /**
     * Recupera el historial completo de mensajes
     * pertenecientes a una conversación privada.
     *
     * @param conversationId El identificador único del chat privado.
     * @return Una lista de mensajes de la conversación, ordenados
     * del más antiguo al más reciente.
     */
    public List<Message> findChatMessages(
            final Long conversationId) {
        return messageRepository
                .findByConversationIdOrderByTimestampAsc(
                        conversationId
                );
    }

    /**
     * Recupera el historial completo de mensajes
     * publicados en un canal de servidor.
     *
     * @param channelId El identificador único del canal público.
     * @return Una lista de mensajes del canal, ordenados
     * del más antiguo al más reciente.
     */
    public List<Message> findChannelMessages(
            final Long channelId) {
        return messageRepository.findByChannelIdOrderByTimestampAsc(channelId);
    }
}
