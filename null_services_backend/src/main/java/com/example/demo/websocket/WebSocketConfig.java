package com.example.demo.websocket;

import com.example.demo.security.JwtService; // 👈 Importamos tu servicio real
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${application.cors.allowed-origins}")
    private String[] allowedOrigins;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

    /**
     * INTERCEPTOR: El "portero" del WebSocket.
     * Atrapa el token JWT que viene desde Angular, lo valida y le da una
     * identidad real (Principal) a la sesión de STOMP.
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                // Solo interceptamos cuando el cliente intenta establecer la conexión inicial
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {

                    // Extraemos el token del header que configuraste en Angular (rxStompConfig)
                    String authHeader = accessor.getFirstNativeHeader("Authorization");

                    if (authHeader != null && authHeader.startsWith("Bearer ")) {
                        String token = authHeader.substring(7);

                        try {
                            // 1. Sacamos el email usando tu JwtService
                            String userEmail = jwtService.extractUsername(token);

                            if (userEmail != null) {
                                // 2. Buscamos al usuario en la BD
                                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                                // 3. Validamos que el token no esté expirado o corrupto
                                if (jwtService.isTokenValid(token, userDetails)) {

                                    // 4. Creamos el objeto de autenticación
                                    UsernamePasswordAuthenticationToken authentication =
                                            new UsernamePasswordAuthenticationToken(
                                                    userDetails,
                                                    null,
                                                    userDetails.getAuthorities()
                                            );

                                    // 5. ¡LA MAGIA! Vinculamos este usuario a la sesión del WebSocket
                                    accessor.setUser(authentication);
                                    System.out.println("✅ WS Autenticado: " + userEmail);
                                }
                            }
                        } catch (Exception e) {
                            // Si el token es inválido o expiró, simplemente no autenticamos la sesión
                            System.err.println("❌ Token JWT inválido en el WebSocket: " + e.getMessage());
                        }
                    } else {
                        System.out.println("⚠️ Intento de conexión WS sin token JWT.");
                    }
                }
                return message;
            }
        });
    }
}