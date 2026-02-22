package com.example.demo.conversation;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConversationResponse {
    private Long id;
    private String otherUserName;
}
