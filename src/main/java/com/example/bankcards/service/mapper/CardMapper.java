package com.example.bankcards.service.mapper;

import com.example.bankcards.dto.CardDtoForAdmin;
import com.example.bankcards.dto.CardDtoForUser;
import com.example.bankcards.entity.Card;

public interface CardMapper {
    CardDtoForAdmin toCardDtoForAdmin(Card card);
    CardDtoForUser toCardDtoForUser(Card card);
}
