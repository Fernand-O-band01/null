package com.example.demo.server;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Objeto de Transferencia de Datos (DTO)
 * utilizado para recibir
 * la información necesaria al crear o modificar
 * un canal desde el cliente.
 * Contiene las validaciones básicas para garantizar
 * la integridad de los datos de entrada.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChannelRequest {

    /**
     * Nombre propuesto para el nuevo canal.
     * Es un campo estrictamente obligatorio y no
     * puede estar vacío ni contener solo espacios.
     */
    @NotBlank(message = "El nombre del canal es obligatorio")
    private String name;

    /**
     * Tipo de canal solicitado
     * (por ejemplo, "TEXT" o "VOICE").
     * Si el frontend no lo especifica,
     * el constructor (Builder) lo inicializa
     * por defecto como un canal de texto.
     */
    @Builder.Default
    private String type = "TEXT";

    /**
     * Indicador de privacidad del canal.
     * Define si el canal se creará con
     * acceso restringido (cantadito) o será
     * público para el servidor.
     */
    private Boolean isPrivate;

}
