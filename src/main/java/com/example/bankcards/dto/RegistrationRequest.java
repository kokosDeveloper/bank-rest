package com.example.bankcards.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegistrationRequest(
        @NotBlank(message = "Email is required")
        @Email(message = "Email is not formatted")
        String email,
        @NotBlank(message = "Password is required")
        String password,
        @NotBlank(message = "First Name is required")
        String firstName,
        @NotBlank(message = "Last Name is required")
        String lastName
) {
}
