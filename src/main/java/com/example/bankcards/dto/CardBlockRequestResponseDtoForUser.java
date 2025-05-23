package com.example.bankcards.dto;

import java.time.LocalDateTime;

public record CardBlockRequestResponseDtoForUser(
        Long id,
        CardDtoForUser cardDtoForUser,
        String reason,
        LocalDateTime requestedAt,
        boolean processed,
        boolean approved
) {
}
