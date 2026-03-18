package com.example.demo.friends;

import com.example.demo.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendsRepository extends JpaRepository<Friends, Long> {

    /**
     * Propósito: Encontrar amistad entre dos usuarios.
     * @param user1 Usuario principal revisando sus
     * posibles amistada o si tiene amistad con otro usuario.
     * @param user2 Usuario principal revisando sus
     * posibles amistada o si tiene amistad con otro usuario.
     * @return Una lista con la amistad entre cada usuario
     */
    @Query("SELECT f FROM Friends f WHERE ("
            +
            "f.requester = :user1 "
            +
            "AND f.addressee = :user2) "
            +
            "OR (f.requester = :user2 "
            +
            "AND f.addressee = :user1)")
    Optional<Friends> findFriendshipBetweenUsers(
            @Param("user1") User user1,
            @Param("user2") User user2);

    /**
     * Busca todas las relaciones o solicitudes de amistad de un usuario
     * filtradas por un estado específico.
     * El usuario puede ser tanto el que envió
     * la solicitud como el que la recibió.
     *
     * @param user El usuario del cual se quieren buscar las amistades.
     * @param status El estado de la relación a filtrar
     * (ej. PENDING, ACCEPTED).
     * @return Una lista de las relaciones de amistad
     * que coinciden con el usuario y el estado.
     */
    @Query("SELECT f FROM Friends f WHERE ("
            +
            "f.requester = :user "
            +
            "OR f.addressee = :user) "
            +
            "AND f.status = :status")
    List<Friends> findAllByUserAndStatus(
            @Param("user") User user,
            @Param("status") FriendShipStatus status);

    /**
     * Busca las solicitudes de amistad que un usuario
     * ha recibido y que se encuentran en un
     * estado específico (generalmente pendientes).
     * Solo devuelve aquellas donde el usuario es
     * el destinatario (addressee).
     *
     * @param user El usuario receptor de las solicitudes.
     * @param status El estado en el que deben estar
     * las solicitudes (ej. PENDING).
     * @return Una lista de las solicitudes de amistad
     * recibidas por el usuario en ese estado.
     */
    @Query("SELECT f FROM Friends f WHERE "
            +
            "f.addressee = :user "
            +
            "AND f.status = :status")
    List<Friends> findPendingRequestForUser(
            @Param("user") User user,
            @Param("status") FriendShipStatus status);
}
