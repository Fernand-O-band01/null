package com.example.demo.voice;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Servicio encargado de gestionar el estado de
 * presencia en los canales de voz en memoria.
 * Mantiene un registro en tiempo real de qué usuarios
 * están conectados a qué canales,
 * utilizando estructuras de datos concurrentes para
 * la integridad en entornos multi-hilo.
 */
@Service
@RequiredArgsConstructor
public final class VoicePresenceService {

    /**
     * Plantilla de mensajería para
     * enviar actualizaciones de estado vía WebSockets.
     */
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Mapa concurrente que almacena el
     * estado global de las salas de voz.
     * Estructura: ServerId
     * -> (ChannelId -> Conjunto de Participantes).
     */
    private final Map<Long, Map<Long, Set<VoiceParticipant>>> activeVoiceRooms =
            new ConcurrentHashMap<>();

    /**
     * Registra la entrada de un participante en
     * una sala de voz y notifica a los suscriptores.
     * Utiliza computeIfAbsent para asegurar
     * la creación segura de mapas anidados.
     *
     * @param serverId El identificador del servidor.
     * @param channelId El identificador del canal de voz.
     * @param participant El objeto con la información
     *                    del usuario que se une.
     */
    public void joinRoom(
            final Long serverId,
            final Long channelId,
            final VoiceParticipant participant) {
        activeVoiceRooms.computeIfAbsent(serverId,
                        k -> new ConcurrentHashMap<>())
                .computeIfAbsent(channelId, k -> ConcurrentHashMap.newKeySet())
                .add(participant);

        broadcastState(serverId);
    }

    /**
     * Elimina a un usuario de una sala de voz y
     * limpia la memoria si el canal queda vacío.
     *
     * @param serverId El identificador del servidor.
     * @param channelId El identificador del canal de voz.
     * @param userId El identificador único del usuario que sale.
     */
    public void leaveRoom(
            final Long serverId,
            final Long channelId,
            final Long userId) {
        Map<Long, Set<VoiceParticipant>> serverRooms
                =
                activeVoiceRooms.get(serverId);

        if (serverRooms != null && serverRooms.containsKey(channelId)) {
            serverRooms.get(channelId).removeIf(
                    p -> p.getUserId().equals(userId));

            if (serverRooms.get(channelId).isEmpty()) {
                serverRooms.remove(channelId);
            }

            broadcastState(serverId);
        }
    }

    /**
     * Emite el estado completo de presencia de voz
     * de un servidor a un tópico STOMP específico.
     * Permite que todos los clientes conectados al
     * servidor vean quién está en cada canal.
     *
     * @param serverId El identificador del servidor
     * cuyo estado se va a difundir.
     */
    public void broadcastState(
            final Long serverId) {
        Map<Long, Set<VoiceParticipant>> serverState =
                activeVoiceRooms.getOrDefault(serverId, new HashMap<>());
        String topic = "/topic/server/" + serverId + "/voice-presence";

        messagingTemplate.convertAndSend(topic, serverState);
    }
}
