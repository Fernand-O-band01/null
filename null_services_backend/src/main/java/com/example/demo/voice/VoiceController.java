package com.example.demo.voice;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Controlador REST encargado de gestionar
 * la emisión de tokens para canales de voz.
 * Actúa como intermediario entre el cliente
 * (Angular) y el servidor de LiveKit,
 * garantizando que solo usuarios autenticados
 * reciban credenciales de acceso.
 */
@RestController
@RequestMapping("/voice")
public class VoiceController {

    /**
     * Servicio encargado de la lógica de
     * generación de tokens de LiveKit.
     */
    private final LiveKitService liveKitService;

    /**
     * Constructor para la inyección de
     * dependencias de los servicios necesarios.
     *
     * @param liveKitServices El servicio de LiveKit para
     * la gestión de tokens de voz.
     */
    public VoiceController(final LiveKitService liveKitServices) {
        this.liveKitService = liveKitServices;
    }

    /**
     * Genera un token de acceso dinámico
     * para un canal de voz específico.
     * Este endpoint es invocado por el cliente
     * antes de intentar establecer
     * la conexión WebRTC con el servidor de medios.
     *
     * @param channelId El identificador del canal al
     * que el usuario desea unirse.
     * @param connectedUser El objeto de autenticación
     * que representa al usuario actual.
     * @return ResponseEntity con un mapa que
     * contiene el token JWT generado.
     */
    @GetMapping("/token/{channelId}")
    public ResponseEntity<Map<String, String>> getVoiceToken(
            final @PathVariable String channelId,
            final Authentication connectedUser) {

        // 1. Extraemos el identificador del usuario
        // desde su token de seguridad
        String username = connectedUser.getName();

        // 2. Solicitamos al servicio la
        // fabricación del token de acceso
        String token = liveKitService.generateVoiceToken(channelId, username);

        // 3. Devolvemos el token envuelto en
        // un objeto JSON para el frontend
        return ResponseEntity.ok(Map.of("token", token));
    }
}
