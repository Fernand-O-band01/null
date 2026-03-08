package com.example.demo.websocket;

import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import com.example.demo.user.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional; // 🚀 Importante
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @EventListener
    @Transactional // 🚀 MUY IMPORTANTE para asegurar que el UPDATE funcione fuera del hilo HTTP
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        System.out.println("🔴 1. Evento de desconexión de socket disparado...");

        Principal principal = event.getUser();
        if (principal == null) {
            System.out.println("❌ 2. El principal es NULL. Sigue siendo anónimo.");
            return;
        }

        System.out.println("👤 2. Usuario detectado en la desconexión: " + principal.getName());

        if (principal instanceof Authentication auth) {
            try {
                // Sacamos al usuario de la sesión
                User userPrincipal = (User) auth.getPrincipal();
                Integer userId = userPrincipal.getId(); // Usa el tipo correcto (Integer o Long)

                System.out.println("🔍 3. Buscando en BD al usuario con ID: " + userId);

                userRepository.findById(userId).ifPresent(user -> {

                    System.out.println("💾 4. Guardando estado OFFLINE en BD para: " + user.getNickName());
                    user.setStatus(UserStatus.OFFLINE);
                    userRepository.save(user);

                    String destination = "/topic/user/status/" + user.getId();
                    System.out.println("📣 5. Enviando mensaje STOMP a: " + destination);
                    messagingTemplate.convertAndSend(destination, "OFFLINE");

                    System.out.println("✅ 6. Desconexión completada con éxito.");
                });

            } catch (ClassCastException e) {
                System.out.println("❌ ERROR: El objeto Principal no es tu entidad User. Es: " + auth.getPrincipal().getClass().getName());
            } catch (Exception e) {
                System.out.println("❌ ERROR INESPERADO: " + e.getMessage());
                e.printStackTrace(); // Esto nos dirá exactamente por qué falla
            }
        }
    }

// ... dentro de la clase ...

    @EventListener
    @Transactional
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Principal principal = accessor.getUser();

        if (principal instanceof Authentication auth) {
            User userPrincipal = (User) auth.getPrincipal();
            Integer userId = userPrincipal.getId();

            userRepository.findById(userId).ifPresent(user -> {
                // 1. Cambiamos estado a ONLINE en la base de datos
                user.setStatus(UserStatus.ONLINE);
                userRepository.save(user);

                // 2. Avisamos a todo el mundo (suscriptores del tópico)
                String destination = "/topic/user/status/" + user.getId();
                messagingTemplate.convertAndSend(destination, "ONLINE");

                System.out.println("🟢 Usuario conectado y anunciado: " + user.getNickName());
            });
        }
    }
}