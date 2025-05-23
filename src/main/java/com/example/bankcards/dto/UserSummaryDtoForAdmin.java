package com.example.bankcards.dto;

public record UserSummaryDtoForAdmin(
        Long id,
        String email,
        String firstName,
        String lastName
) {
}
