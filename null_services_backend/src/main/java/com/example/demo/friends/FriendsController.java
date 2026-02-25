package com.example.demo.friends;

import com.example.demo.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendsController {

    private final FriendsService friendsService;

    @PostMapping("/request/{targetUserId}")
    public ResponseEntity<String> requestFriends(
            @PathVariable("targetUserId") Integer targetUserId,
            Authentication connectedUser
    ){
        friendsService.sendFriendRequest(targetUserId, connectedUser);
        return ResponseEntity.ok("Friend request has been sent");
    }

    @PatchMapping("/accept/{friendshipId}")
    public ResponseEntity<String> acceptFriendRequest(
            @PathVariable("friendshipId") Long friendshipId,
            Authentication connectedUser
    ){
        friendsService.acceptFriendRequest(friendshipId, connectedUser);
        return ResponseEntity.ok("Friend request has been accepted");
    }

    @GetMapping
    public ResponseEntity<List<FriendResponseDTO>> getMyFriends(
            Authentication connectedUser
    ){
        List<FriendResponseDTO> friends = friendsService.getMyFriends(connectedUser);
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<FriendRequestDTO>> getPendingRequests(
            Authentication connectedUser
    ){
        List<FriendRequestDTO> pendingRequest = friendsService.getMyPendingRequests(connectedUser);
        return ResponseEntity.ok(pendingRequest);
    }

}
