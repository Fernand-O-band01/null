package com.example.demo.kafka;

import com.example.demo.message.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageConsumer {

    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "chat-messages", groupId = "null-group")
    public void consume(Message message) {

        // 🚀 VERIFICACIÓN DE ENRUTAMIENTO (ROUTING)
        // Revisamos si el mensaje tiene un ID de canal (Es para un Servidor)
        if (message.getChannelId() != null) {
            log.info("📢 Recibido de Kafka (Canal): '{}' para channelId: {}", message.getContent(), message.getChannelId());

            // 💡 IMPORTANTE: Enviamos a la ruta de canales
            String destination = "/topic/channel/" + message.getChannelId();
            messagingTemplate.convertAndSend(destination, message);

        }
        // Si no es de canal, revisamos si tiene ID de conversación (Es Chat Privado)
        else if (message.getConversationId() != null) {
            log.info("💬 Recibido de Kafka (Chat Privado): '{}' para conversationId: {}", message.getContent(), message.getConversationId());

            // 💡 IMPORTANTE: Enviamos a la ruta de chat privado
            String destination = "/topic/chat/" + message.getConversationId();
            messagingTemplate.convertAndSend(destination, message);

        } else {
            log.warn("⚠️ Mensaje huérfano recibido en Kafka: no tiene channelId ni conversationId");
        }
    }
}