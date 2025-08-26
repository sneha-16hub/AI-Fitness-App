package com.fitness.gateway.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Email is blank")
    @Email(message = "invalid Mail format")
    private String email;
    private String keycloakId;
    @NotBlank(message = "Password is blank")
    @Size(message = "Password must have min 6 characters")
    private String password;
    private String firstName;
    private String lastName;

}
