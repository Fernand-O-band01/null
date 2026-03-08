package com.example.demo.voice;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class VoicePresenceService {

    private final SimpMessagingTemplate messagingTemplate;

    // 🌍 El Mapa Maestro: ServerId -> (ChannelId -> Lista de Participantes)
    private final Map<Long, Map<Long, Set<VoiceParticipant>>> activeVoiceRooms = new ConcurrentHashMap<>();

    public void joinRoom(Long serverId, Long channelId, VoiceParticipant participant) {
        // 1. Buscamos el servidor (si no existe, lo creamos en RAM)
        activeVoiceRooms.computeIfAbsent(serverId, k -> new ConcurrentHashMap<>())
                // 2. Buscamos el canal (si no existe, lo creamos)
                .computeIfAbsent(channelId, k -> ConcurrentHashMap.newKeySet())
                // 3. Agregamos al usuario
                .add(participant);

        // 4. Avisamos a todos en el servidor
        broadcastState(serverId);
    }

    public void leaveRoom(Long serverId, Long channelId, Long userId) {
        Map<Long, Set<VoiceParticipant>> serverRooms = activeVoiceRooms.get(serverId);

        if (serverRooms != null && serverRooms.containsKey(channelId)) {
            // Eliminamos al usuario de la lista
            serverRooms.get(channelId).removeIf(p -> p.getUserId().longValue() == userId.longValue());

            // Si el canal quedó vacío, limpiamos la memoria
            if (serverRooms.get(channelId).isEmpty()) {
                serverRooms.remove(channelId);
            }

            // Avisamos a todos
            broadcastState(serverId);
        }
    }

    // 📢 El Megáfono: Envía el estado completo del servidor por STOMP
    public void broadcastState(Long serverId) {
        Map<Long, Set<VoiceParticipant>> serverState = activeVoiceRooms.getOrDefault(serverId, new HashMap<>());
        String topic = "/topic/server/" + serverId + "/voice-presence";

        messagingTemplate.convertAndSend(topic, serverState);
    }
}