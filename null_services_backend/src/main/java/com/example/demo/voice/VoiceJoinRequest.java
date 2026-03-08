package com.example.demo.voice;

import lombok.Data;

@Data
public class VoiceJoinRequest {

    private Long channelId;
    private Long userId;
    private String username;
    private String imageUrl;

}
