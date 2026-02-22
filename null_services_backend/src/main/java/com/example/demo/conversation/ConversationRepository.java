package com.example.demo.conversation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    @Query("SELECT c FROM Conversation c JOIN c.participants p WHERE p.id = :userId")
    List<Conversation> findAllByUserId(@Param("userId") Integer userId);
    @Query("SELECT c FROM Conversation c JOIN c.participants p1 JOIN c.participants p2 WHERE p1.id = :userId AND p2.id = :user2Id AND size(c.participants) = 2")
    Optional<Conversation> findConversationByUsers(@Param("userId") Integer userId, @Param("user2Id") Integer user2Id);

}
