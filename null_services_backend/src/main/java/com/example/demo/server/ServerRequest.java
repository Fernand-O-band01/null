package com.example.demo.server;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServerRequest {
    @NotBlank(message = "Server's name is required")
    private String name;
    private String imageUrl;

}
