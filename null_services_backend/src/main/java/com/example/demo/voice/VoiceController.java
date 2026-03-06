package com.example.demo.voice;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/voice") // 🚀 Recuerda que tu context-path ya le agrega /api/v1
public class VoiceController {

    private final LiveKitService liveKitService;

    // Inyección de dependencias
    public VoiceController(LiveKitService liveKitService) {
        this.liveKitService = liveKitService;
    }

    /**
     * Endpoint que Angular llamará antes de conectarse al servidor de LiveKit.
     */
    @GetMapping("/token/{channelId}")
    public ResponseEntity<Map<String, String>> getVoiceToken(
            @PathVariable String channelId,
            Authentication connectedUser // 🚀 Spring Security nos da el usuario actual
    ) {
        // 1. Sacamos el email o ID del usuario desde su JWT (gracias a tu filtro de seguridad)
        String username = connectedUser.getName();

        // 2. Le pedimos a tu servicio que fabrique el boleto VIP
        String token = liveKitService.generateVoiceToken(channelId, username);

        // 3. Se lo entregamos a Angular en un JSON bonito: { "token": "ey..." }
        return ResponseEntity.ok(Map.of("token", token));
    }
}