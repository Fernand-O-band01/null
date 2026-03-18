package com.example.demo.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Entidad (Documento) que representa un mensaje
 * de chat almacenado en MongoDB.
 * Soporta tanto mensajes directos (conversaciones privadas)
 * como mensajes de canales en servidores.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "messages")

@CompoundIndexes({
        @CompoundIndex(
                name = "conv_time_idx",
                def = "{'conversationId': 1, 'timestamp': 1}"),
        @CompoundIndex(
                name = "channel_time_idx",
                def = "{'channelId': 1, 'timestamp': 1}")
})
public class Message {

    /**
     * Identificador único del mensaje
     * generado automáticamente por MongoDB.
     */
    @Id
    private String id;

    /**
     * Contenido de texto del mensaje enviado.
     */
    private String content;

    /**
     * Identificador único del usuario que
     * envía el mensaje (remitente).
     */
    private Integer sendId;

    /**
     * Apodo o nombre público del usuario remitente
     * en el momento de enviar el mensaje.
     */
    private String senderNickname;

    /**
     * Identificador de la conversación privada.
     * Si este campo tiene valor, significa que
     * es un Mensaje Directo (MD).
     */
    private Long conversationId;

    /**
     * Identificador del canal dentro de un servidor.
     * Si este campo tiene valor, significa que es un
     * mensaje público de servidor.
     */
    private Long channelId;

    /**
     * Fecha y hora exacta en la que se generó y guardó el mensaje.
     */
    private LocalDateTime timestamp;

}
