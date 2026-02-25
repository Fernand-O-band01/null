package com.example.demo.friends;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendRequestDTO {

    private Long friendshipId;
    private Integer requesterId;
    private String requesterNickname;
    private LocalDateTime createdAt;
}
