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

    @Query("SELECT f FROM Friends f WHERE (f.requester = :user1 AND f.addressee = :user2) OR (f.requester = :user2 AND f.addressee = :user1)")
    Optional<Friends> findFriendshipBetweenUsers(@Param("user1") User user1, @Param("user2") User user2);

    @Query("SELECT f FROM Friends f WHERE (f.requester = :user OR f.addressee = :user) AND f.status = :status")
    List<Friends> findAllByUserAndStatus(@Param("user") User user, @Param("status") FriendShipStatus status );

    @Query("SELECT f FROM Friends f WHERE f.addressee = :user AND f.status = :status")
    List<Friends> findPendingRequestForUser(@Param("user") User user, @Param("status") FriendShipStatus status);
}
