package com.example.bankcards.dto;

import com.example.bankcards.entity.Role;

import java.util.List;

public record UserDto(
        Long id,
        String email,
        String firstName,
        String lastName,
        Role role,
        List<CardDtoForAdmin> cards
) {
}
