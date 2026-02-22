package com.example.demo.conversation;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/conversations")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;

    @PostMapping("/{targetUserId}")
    public ResponseEntity<ConversationResponse> createConversation(
            @PathVariable Integer targetUserId,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(
                conversationService.createOrGetConversation(
                        targetUserId,
                        connectedUser
                )
        );
    }

}
