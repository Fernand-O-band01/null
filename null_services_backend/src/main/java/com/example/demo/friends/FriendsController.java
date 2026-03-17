package com.example.demo.friends;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;
import java.util.Map;

/**
 * Controlador dedicado al manejo de endpoints de
 * Peticiones de amistad (ENVIAR, ACEPTAR,
 * OBTENER AMIGOS, PENDIENTES).
 */

@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendsController {

    /**
     * Servicio encargado de manejar la lógica para el
     * envío de cada petición.
     */
    private final FriendsService friendsService;

    /**
     * ENDPOINT: POST /request/{targetUserId}.
     * Propósito: Enviar solicitud de amistad.
     * @param connectedUser El usuario que está haciendo
     * la petición (obtenido del token JWT).
     * @param targetUserId Usuario seleccionado para enviar
     * la solicitud de amistad.
     * @return Mensaje confirmando el envío de la solicitud
     * de manera exitosa.
     */
    @PostMapping("/request/{targetUserId}")
    public ResponseEntity<Map<String, String>> requestFriends(
            @PathVariable("targetUserId") final Integer targetUserId,
            final Authentication connectedUser
    ) {
        friendsService.sendFriendRequest(targetUserId, connectedUser);

        return ResponseEntity.ok(Map.of(
                "message", "Friend request has been sent"
                )
        );
    }

    /**
     * ENDPOINT: PATCH /accept/{friendshipId}.
     * Propósito: Aceptar solicitud de amistad.
     * @param connectedUser El usuario que está haciendo
     * la petición (obtenido del token JWT).
     * @param friendshipId Usuario seleccionado para
     * aceptar la solicitud de amistad.
     * @return Mensaje confirmando que la solicitud ha
     * sido aceptada correctamente.
     */
    @PatchMapping("/accept/{friendshipId}")
    public ResponseEntity<Map<String, String>> acceptFriendRequest(
            @PathVariable("friendshipId") final Long friendshipId,
            final Authentication connectedUser
    ) {
        friendsService.acceptFriendRequest(friendshipId, connectedUser);
        return ResponseEntity.ok(Map.of(
                "message", "Friend request has been accepted"
                )
        );
    }

    /**
     * ENDPOINT: GET.
     * Propósito: Obtener mis amigos.
     * @param connectedUser El usuario que está haciendo
     * la petición (obtenido del token JWT).
     * @return ResponseEntity "OK" con los amigos del
     * usuario
     */
    @GetMapping
    public ResponseEntity<List<FriendResponseDTO>> getMyFriends(
            final Authentication connectedUser
    ) {
        List<FriendResponseDTO> friends = friendsService
                .getMyFriends(connectedUser);
        return ResponseEntity.ok(friends);
    }

    /**
     * ENDPOINT: GET.
     * Propósito: Obtener solicitudes pendientes.
     * @param connectedUser El usuario que está haciendo
     * la petición (obtenido del token JWT).
     * @return ResponseEntity "OK" y
     * Una lista con todas las solicitudes pendientes
     * por aceptar
     */
    @GetMapping("/pending")
    public ResponseEntity<List<FriendRequestDTO>> getPendingRequests(
            final Authentication connectedUser
    ) {
        List<FriendRequestDTO> pendingRequest = friendsService
                .getMyPendingRequests(connectedUser);
        return ResponseEntity.ok(pendingRequest);
    }

}
