package com.example.demo.voice;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(of = "userId")
public class VoiceParticipant {

    private Long userId;
    private String username;
    private String imageUrl;
}
