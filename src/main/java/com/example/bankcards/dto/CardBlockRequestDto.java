package com.example.bankcards.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CardBlockRequestDto(
        @NotNull(message = "Card id to block is required")
        Long cardId,
        @NotBlank(message = "Reason for blocking card is required")
        String reason
) {
}
