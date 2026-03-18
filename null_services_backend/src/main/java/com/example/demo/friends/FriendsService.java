package com.example.demo.friends;

import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendsService {

    /**
     * Repositorio para acceder a las amistades
     * de los usuarios.
     */
    private final FriendsRepository friendsRepository;
    /**
     * Repositorio para acceder a la información de
     * los usuarios.
     */
    private final UserRepository userRepository;


    /**
     * LÓGICA: ENVIAR UNA SOLICITUD DE AMISTAD.
     * Enviar una solicitud de amistada a un usuario.
     * @param connectedUser Usuario activo validado desde el token
     * de seguridad.
     * @param targetUserId Usuario seleccionado para enviar
     * la solicitud de amistad.
     */
    public void sendFriendRequest(
            final Integer targetUserId,
            final Authentication connectedUser
    ) {

        User currentUser = (User) connectedUser.getPrincipal();

        if (currentUser.getId().equals(targetUserId)) {
            throw new RuntimeException(
                    "You can't send friend request to yourself");
        }

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<Friends> existingRelationship = friendsRepository
                .findFriendshipBetweenUsers(currentUser, targetUser);

        if (existingRelationship.isPresent()) {
            throw new RuntimeException(
                    "Friendship already exists or has already been sent");
        }

        Friends request = Friends.builder()
                .requester(currentUser)
                .addressee(targetUser)
                .status(FriendShipStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        friendsRepository.save(request);
    }

    /**
     * LÓGICA: ACEPTAR UNA SOLICITUD DE AMISTAD.
     * Aceptar una solicitud de amistada proveniente de un
     * usuario desconocido.
     * @param connectedUser Usuario activo validado desde el token
     * de seguridad.
     * @param friendShipId Identificador unico de la petición de
     * amistada enviada por el usuario.
     */
    public void acceptFriendRequest(
            final Long friendShipId,
            final Authentication connectedUser
    ) {
        User currentUser = (User) connectedUser.getPrincipal();

        Friends request = friendsRepository.findById(friendShipId)
                .orElseThrow(() -> new RuntimeException(
                        "Friendship request not found"));

        if (!request.getAddressee().getId().equals(currentUser.getId())) {
            throw new RuntimeException(
                    "You can't accept friendship request");
        }
        if (request.getStatus() != FriendShipStatus.PENDING) {
            throw new RuntimeException(
                    "Friendship request has already been processed");
        }

        request.setStatus(FriendShipStatus.ACCEPTED);
        friendsRepository.save(request);
    }

    /**
     * LÓGICA: OBTENER TODA LA LISTA DE AMIGOS.
     * Mostar la lista de amigos que posee cada usuario.
     * @param connectedUser Usuario activo validado desde el token
     * de seguridad
     * @return Una lista con todos los amigos que posee el usuario.
     */
    public List<FriendResponseDTO> getMyFriends(
            final Authentication connectedUser
    ) {
        User currentUser = (User) connectedUser.getPrincipal();

        List<Friends> acceptedFriendShip = friendsRepository
                .findAllByUserAndStatus(currentUser, FriendShipStatus.ACCEPTED);

        return acceptedFriendShip.stream()
                .map(friendship -> {
                    User friend = friendship.getRequester()
                            .getId().equals(currentUser.getId())
                            ? friendship.getAddressee()
                            : friendship.getRequester();
                    return mapToFriendResponseDTO(friend);
                })
                .collect(Collectors.toList());
    }

    /**
     * Convierte una entidad User en un objeto
     * de transferencia de datos (FriendResponseDTO).
     * Se utiliza para enviar al cliente únicamente
     * la información pública y necesaria de un amigo.
     *
     * @param user La entidad del usuario que se desea mapear.
     * @return Un objeto FriendResponseDTO con los datos
     * básicos del amigo listos para el frontend.
     */
    private FriendResponseDTO mapToFriendResponseDTO(
            final User user
    ) {
        return FriendResponseDTO.builder()
                .id(user.getId())
                .name(user.getNickName())
                .status(user.getStatus())
                .build();
    }

    /**
     * LÓGICA: OBTENER LAS PETICIONES DE AMISTADES PENDIENTES.
     * Busca si existen peticiones de amistad para el usuario.
     * @param connectedUser Usuario activo validado desde el token
     * de seguridad.
     * @return Una lista con todos las peticiones de amistad
     * pendiente por aceptar o rechazar.
     */
    public List<FriendRequestDTO> getMyPendingRequests(
            final Authentication connectedUser
    ) {
        User currentUser = (User) connectedUser.getPrincipal();

        List<Friends> pendingRequests = friendsRepository
                .findPendingRequestForUser(
                        currentUser, FriendShipStatus.PENDING);

        return pendingRequests.stream()
                .map(request -> FriendRequestDTO.builder()
                        .friendshipId(request.getId())
                        .requesterId(request.getRequester().getId())
                        .requesterNickname(request.getRequester().getNickName())
                        .createdAt(request.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}

