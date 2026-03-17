package com.example.demo.friends;

import com.example.demo.user.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class FriendResponseDTO {

    /**
     * Identificador del usuario amigo.
     */
    private Integer id;
    /**
     * Nombre del amigo.
     */
    private String name;
    /**
     * Estado actual
     * (EN LINEA, LEJOS, NO MOLESTAR, DESCONECTADO).
     */
    private UserStatus status;
}
