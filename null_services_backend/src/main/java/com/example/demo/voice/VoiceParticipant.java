package com.example.demo.voice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Representa a un participante activo
 * dentro de una sesión de voz de LiveKit.
 * Esta clase se utiliza para gestionar la lista
 * de usuarios conectados en tiempo real
 * y asegurar que no existan duplicados mediante
 * la comparación de sus identificadores.
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(of = "userId")
public class VoiceParticipant {

    /**
     * Identificador único del usuario que
     * participa en la sesión de voz.
     * Se utiliza como la base principal
     * para la comparación de igualdad (equals).
     */
    private Long userId;

    /**
     * Nombre de usuario o apodo que se muestra
     * en la interfaz de la sala de voz.
     */
    private String username;

    /**
     * URL de la imagen de perfil o avatar del
     * participante para su visualización.
     */
    private String imageUrl;

}
