package com.example.demo.friends;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Objeto de transferencia de datos (DTO) que
 * representa la respuesta de una conversación.
 * Contiene la información pública y básica que se envía
 * al cliente (frontend).
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendRequestDTO {

    /**
     * Identificador único de la petición de amistad.
     */
    private Long friendshipId;
    /**
     * Identificador único del remitente de la
     * petición.
     */
    private Integer requesterId;
    /**
     * Nickname proveniente del remitente.
     */
    private String requesterNickname;
    /**
     * Fecha y ahora al momento que fue enviada
     * la petición de amistad.
     */
    private LocalDateTime createdAt;
}
