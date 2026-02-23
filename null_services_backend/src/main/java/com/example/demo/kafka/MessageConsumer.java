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
        log.info("Received message from kafka '{}' from room: {}",
                message.getContent(),
                message.getConversationId());

        String destination = "/topic/chat/" + message.getConversationId();

        messagingTemplate.convertAndSend(destination, message);
    }
}