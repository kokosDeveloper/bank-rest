package com.example.bankcards.dto;

import jakarta.validation.constraints.NotNull;

public record CardCreateUpdateRequest(
        @NotNull(message = "User Id is required")
        Long userId
) {
}
