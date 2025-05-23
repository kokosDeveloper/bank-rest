package com.example.bankcards.dto;

import java.time.LocalDateTime;

public record CardBlockRequestResponseDtoForAdmin(
        Long id,
        UserSummaryDtoForAdmin userDto,
        CardDtoForAdmin cardDtoForAdmin,
        String reason,
        LocalDateTime requestedAt,
        boolean processed,
        boolean approved
) {
}
