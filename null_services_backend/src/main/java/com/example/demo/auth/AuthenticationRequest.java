package com.example.demo.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticationRequest {
    @Email(message = "Bad format to email address")
    @NotEmpty(message = "It must no be empty")
    @NotBlank(message = "Learn how to type , DONT BLANK SPACES")
    private String username;
    @NotEmpty(message = "It must no be empty")
    @NotBlank(message = "Learn how to type , DONT BLANK SPACES")
    @Size(min = 8, message = "Generic password will not accepted")
    private String password;

}
