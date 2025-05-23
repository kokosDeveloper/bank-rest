package com.example.bankcards.service.mapper.impl;

import com.example.bankcards.dto.CardBlockRequestResponseDtoForAdmin;
import com.example.bankcards.dto.CardBlockRequestResponseDtoForUser;
import com.example.bankcards.entity.CardBlockRequest;
import com.example.bankcards.service.mapper.CardBlockRequestMapper;
import com.example.bankcards.service.mapper.CardMapper;
import com.example.bankcards.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardBlockRequestMapperImpl implements CardBlockRequestMapper {
    private final UserMapper userMapper;
    private final CardMapper cardMapper;
    @Override
    public CardBlockRequestResponseDtoForAdmin toDtoForAdmin(CardBlockRequest cardBlockRequest) {
        return new CardBlockRequestResponseDtoForAdmin(
                cardBlockRequest.getId(),
                userMapper.toUserSummaryDto(cardBlockRequest.getUser()),
                cardMapper.toCardDtoForAdmin(cardBlockRequest.getCard()),
                cardBlockRequest.getReason(),
                cardBlockRequest.getRequestedAt(),
                cardBlockRequest.isProcessed(),
                cardBlockRequest.isApproved());
    }

    @Override
    public CardBlockRequestResponseDtoForUser toDtoForUser(CardBlockRequest cardBlockRequest) {
        return new CardBlockRequestResponseDtoForUser(
                cardBlockRequest.getId(),
                cardMapper.toCardDtoForUser(cardBlockRequest.getCard()),
                cardBlockRequest.getReason(),
                cardBlockRequest.getRequestedAt(),
                cardBlockRequest.isProcessed(),
                cardBlockRequest.isApproved()
        );
    }
}
