package com.example.demo.server;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Objeto de Transferencia de Datos (DTO)
 * utilizado para enviar
 * la información de un canal desde
 * el servidor hacia el cliente.
 * Separa la capa de presentación de la entidad
 * real de la base de datos.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChannelResponse {

    /**
     * Identificador único del canal, necesario
     * para que el frontend
     * pueda enrutar mensajes o realizar modificaciones.
     */
    private Long id;

    /**
     * Nombre descriptivo del canal que se
     * mostrará en la interfaz de usuario.
     */
    private String name;

    /**
     * Tipo de canal devuelto
     * (por ejemplo, "TEXT" o "VOICE"),
     * útil para que el cliente renderice
     * diferentes iconos o interfaces.
     */
    private String type;

    /**
     * Indicador de privacidad del canal.
     * Le informa al frontend si debe
     * renderizar un icono de candado junto al nombre.
     */
    private Boolean isPrivate;

}
