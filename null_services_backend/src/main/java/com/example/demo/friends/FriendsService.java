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

    private final FriendsRepository friendsRepository;
    private final UserRepository userRepository;

    public void sendFriendRequest(Integer targetUserId, Authentication connectedUser){

        User currentUser = (User) connectedUser.getPrincipal();

        if(currentUser.getId().equals(targetUserId)){
            throw new RuntimeException("You can't send friend request to yourself");
        }

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<Friends> existingRelationship = friendsRepository.findFriendshipBetweenUsers(currentUser, targetUser);

        if(existingRelationship.isPresent()){
            throw new RuntimeException("Friendship already exists or request has already been sent");
        }

        Friends request = Friends.builder()
                .requester(currentUser)
                .addressee(targetUser)
                .status(FriendShipStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        friendsRepository.save(request);
    }

    public void acceptFriendRequest(Long friendShipId, Authentication connectedUser){
        User currentUser = (User) connectedUser.getPrincipal();

        Friends request = friendsRepository.findById(friendShipId)
                .orElseThrow(() -> new RuntimeException("Friendship request not found"));

        if(!request.getAddressee().getId().equals(currentUser.getId())){
            throw new RuntimeException("You can't accept friendship request");
        }
        if(request.getStatus() != FriendShipStatus.PENDING){
            throw new RuntimeException("Friendship request has already been processed");
        }

        request.setStatus(FriendShipStatus.ACCEPTED);
        friendsRepository.save(request);
    }

    public List<FriendResponseDTO> getMyFriends(Authentication connectedUser){
        User currentUser = (User) connectedUser.getPrincipal();

        List<Friends> acceptedFriendShip = friendsRepository.findAllByUserAndStatus(currentUser, FriendShipStatus.ACCEPTED);

        return acceptedFriendShip.stream()
                .map(friendship -> {
                    User friend = friendship.getRequester().getId().equals(currentUser.getId())
                            ? friendship.getAddressee()
                            : friendship.getRequester();
                    return mapToFriendResponseDTO(friend);
                })
                .collect(Collectors.toList());
    }

    private FriendResponseDTO mapToFriendResponseDTO(User user) {
        return FriendResponseDTO.builder()
                .id(user.getId())
                .name(user.getNickName())
                .status(user.getStatus())
                .build();
    }

    public List<FriendRequestDTO> getMyPendingRequests(Authentication connectedUser){
        User currentUser = (User) connectedUser.getPrincipal();

        List<Friends> pendingRequests = friendsRepository.findPendingRequestForUser(currentUser, FriendShipStatus.PENDING);

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
