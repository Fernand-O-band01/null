package com.example.demo.user;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate; // 👈 Importante
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate; // 👈 Inyectamos el "mensajero"

    public void updatePresenceStatus(UserStatus newStatus, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        user.setStatus(newStatus);
        userRepository.save(user);

        // 📣 NOTIFICACIÓN EN TIEMPO REAL
        // Enviamos el nuevo estado a un "topic" único basado en el ID del usuario.
        // Ejemplo de destino: /topic/user/status/5
        String destination = "/topic/user/status/" + user.getId();

        // Enviamos solo el nuevo estado (ej: "AWAY")
        messagingTemplate.convertAndSend(destination, newStatus.toString());

        System.out.println("WebSocket emitido a: " + destination + " con valor: " + newStatus);
    }
}