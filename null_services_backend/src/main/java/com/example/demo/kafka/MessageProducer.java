package com.example.demo.kafka;

import com.example.demo.message.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Servicio productor de Kafka encargado
 * de publicar los mensajes de chat en el broker.
 * Funciona como el punto de entrada para
 * que el backend envíe los mensajes hacia
 * la cola de procesamiento asíncrono.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageProducer {

    /**
     * Plantilla principal de Spring Kafka
     * utilizada para enviar los eventos y mensajes al broker.
     */
    private final KafkaTemplate<String, Message> kafkaTemplate;

    /**
     * Nombre constante del topic de Kafka al
     * cual se enviarán todos los mensajes de chat.
     */
    private static final String TOPIC = "chat-messages";

    /**
     * Publica un nuevo mensaje de chat en el
     * tópico configurado de Kafka.
     * Utiliza el ID de la conversación como
     * clave de enrutamiento (partition key)
     * para mantener el orden de los mensajes
     * en el mismo chat.
     *
     * @param message El objeto de mensaje que
     * contiene el texto, el remitente y los IDs de destino.
     */
    public void sendMessage(
            final Message message) {
        log.info("Enviando mensaje a Kafka para la sala {}: {}",
                message.getConversationId(), message.getContent());
        kafkaTemplate.send(TOPIC, String.valueOf(
                message
                        .getConversationId()), message);
    }

}
