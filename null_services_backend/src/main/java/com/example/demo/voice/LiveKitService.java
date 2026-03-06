package com.example.demo.voice; // Ajusta a tu paquete

import io.livekit.server.AccessToken;
import io.livekit.server.RoomJoin;
import io.livekit.server.RoomName;
import org.springframework.stereotype.Service;

@Service
public class LiveKitService {

    // 🚀 Estas son las llaves que Docker configuró automáticamente con el --dev
    private final String API_KEY = "devkey";
    private final String API_SECRET = "secret";

    /**
     * Fabrica un boleto VIP para que un usuario entre a un canal de voz.
     * * @param channelId El ID del canal (ej. "canal-voz-5")
     * @param username El nombre o ID del usuario (ej. "Juan123")
     * @return El Token JWT en formato texto
     */
    public String generateVoiceToken(String channelId, String username) {

        // 1. Creamos un token vacío con nuestras llaves maestras
        AccessToken token = new AccessToken(API_KEY, API_SECRET);

        // 2. Le ponemos el nombre del usuario
        token.setName(username);
        token.setIdentity(username); // Identity es el ID único en LiveKit

        // 3. Le damos permiso EXCLUSIVO para unirse a la sala de este Canal
        token.addGrants(new RoomJoin(true), new RoomName("canal-" + channelId));

        // 4. Firmamos y devolvemos el boleto
        return token.toJwt();
    }
}