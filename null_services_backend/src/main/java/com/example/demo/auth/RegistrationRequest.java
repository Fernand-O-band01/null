package com.example.demo.auth;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class RegistrationRequest {
    @NotEmpty(message = "Email is required")
    @NotBlank(message = "Blank spaces not accepted")
    private String email;
    @NotEmpty(message = "This form is required")
    @NotBlank(message = "Blank spaces not accepted")
    private String fullname;
    @NotEmpty(message = "This form is required")
    @NotBlank(message = "Blank spaces not accepted")
    private String nickName;
    @NotEmpty(message = "This form is required")
    @NotBlank(message = "Blank spaces not accepted")
    private String password;
    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

}
