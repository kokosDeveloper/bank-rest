package com.example.bankcards.service.mapper;

import com.example.bankcards.dto.CardBlockRequestResponseDtoForAdmin;
import com.example.bankcards.dto.CardBlockRequestResponseDtoForUser;
import com.example.bankcards.entity.CardBlockRequest;

public interface CardBlockRequestMapper {
    CardBlockRequestResponseDtoForAdmin toDtoForAdmin(CardBlockRequest cardBlockRequest);
    CardBlockRequestResponseDtoForUser toDtoForUser(CardBlockRequest cardBlockRequest);
}
