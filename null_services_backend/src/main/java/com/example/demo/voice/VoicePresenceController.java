package com.example.demo.voice;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class VoicePresenceController {

    private final VoicePresenceService presenceService;

    // 🚀 Angular mandará el mensaje a: /app/server/{serverId}/voice/join
    @MessageMapping("/server/{serverId}/voice/join")
    public void joinVoice(@DestinationVariable Long serverId, VoiceJoinRequest request) {

        VoiceParticipant participant = new VoiceParticipant(
                request.getUserId(),
                request.getUsername(),
                request.getImageUrl()
        );

        presenceService.joinRoom(serverId, request.getChannelId(), participant);
    }

    // 🚀 Angular mandará el mensaje a: /app/server/{serverId}/voice/leave
    @MessageMapping("/server/{serverId}/voice/leave")
    public void leaveVoice(@DestinationVariable Long serverId, VoiceJoinRequest request) {

        presenceService.leaveRoom(serverId, request.getChannelId(), request.getUserId());
    }

    // 🚀 NUEVO: Endpoint para que Angular pida la foto actual de las salas
    @MessageMapping("/server/{serverId}/voice/sync")
    public void syncVoice(@DestinationVariable Long serverId) {
        presenceService.broadcastState(serverId);
    }
}