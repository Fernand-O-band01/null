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

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final MessageProducer messageProducer;
    private final UserRepository userRepository;

    /**
     * Guarda y difunde un mensaje enriquecido con el nickname del autor.
     * @param message El mensaje proveniente del cliente.
     * @return El mensaje persistido en MongoDB con el nickname incluido.
     */
    public Message saveMessage(Message message) {

        // 1. Validaciones de Destino (Guardias de Tráfico)
        if (message.getConversationId() != null) {
            if(!conversationRepository.existsById(message.getConversationId())){
                throw new RuntimeException("Error: La conversación de MD no existe.");
            }
        } else if (message.getChannelId() == null) {
            throw new IllegalArgumentException("El mensaje debe tener un channelId o un conversationId.");
        }

        // 2. ENRIQUECIMIENTO (Aquí usamos tu UserRepository JPA)
        // Buscamos al autor por su sendId (Integer)
        String nickname = userRepository.findById(message.getSendId())
                .map(User::getNickName) // 🚀 Usamos getNickName() como en tu ConversationService
                .orElse("Usuario " + message.getSendId());

        // 3. Seteamos los metadatos necesariosSS
        message.setSenderNickname(nickname);
        message.setTimestamp(LocalDateTime.now());

        // 4. Persistencia en MongoDB
        Message savedMessage = messageRepository.save(message);

        // 5. Difusión en tiempo real vía Kafka
        messageProducer.sendMessage(savedMessage);

        log.info("Mensaje de {} enviado con éxito al destino", nickname);

        return savedMessage;
    }

    // El que ya tenías
    public List<Message> findChatMessages(Long conversationId){
        return messageRepository.findByConversationIdOrderByTimestampAsc(conversationId);
    }

    // 🚀 NUEVO: Buscar mensajes del canal
    public List<Message> findChannelMessages(Long channelId){
        return messageRepository.findByChannelIdOrderByTimestampAsc(channelId);
    }
}