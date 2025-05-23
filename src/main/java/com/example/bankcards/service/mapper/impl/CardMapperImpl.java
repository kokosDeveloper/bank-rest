package com.example.bankcards.service.mapper.impl;

import com.example.bankcards.dto.CardDtoForAdmin;
import com.example.bankcards.dto.CardDtoForUser;
import com.example.bankcards.entity.Card;
import com.example.bankcards.service.mapper.CardMapper;
import com.example.bankcards.util.CardNumberEncrypter;
import org.springframework.stereotype.Service;

@Service
public class CardMapperImpl implements CardMapper {
    @Override
    public CardDtoForAdmin toCardDtoForAdmin(Card card) {
        String encryptedNumber = encryptNumber(card.getNumber());
        return new CardDtoForAdmin(
                card.getId(),
                encryptedNumber,
                card.getUser().getId(),
                card.getExpirationDate(),
                card.getMoneyAmount(),
                card.getStatus()
        );
    }

    @Override
    public CardDtoForUser toCardDtoForUser(Card card) {
        String encryptedNumber = encryptNumber(card.getNumber());
        return new CardDtoForUser(
                card.getId(),
                encryptedNumber,
                card.getExpirationDate(),
                card.getMoneyAmount(),
                card.getStatus()
        );
    }

    private String encryptNumber(String number){
        return CardNumberEncrypter.encrypt(number);
    }
}
