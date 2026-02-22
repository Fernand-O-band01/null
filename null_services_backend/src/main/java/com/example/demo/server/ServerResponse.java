package com.example.demo.server;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ServerResponse {
    private Long id;
    private String name;
    private String imageUrl;
}
