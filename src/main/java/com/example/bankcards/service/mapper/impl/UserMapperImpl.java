package com.example.bankcards.service.mapper.impl;

import com.example.bankcards.dto.CardDtoForAdmin;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.dto.UserSummaryDtoForAdmin;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.service.mapper.CardMapper;
import com.example.bankcards.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {
    private final CardMapper cardMapper;
    @Override
    public UserSummaryDtoForAdmin toUserSummaryDto(User user) {
        return new UserSummaryDtoForAdmin(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName());
    }

    @Override
    public UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                mapCards(user.getCards())
        );
    }

    private List<CardDtoForAdmin> mapCards(List<Card> cards){
        return cards.stream()
                .map(cardMapper::toCardDtoForAdmin)
                .toList();
    }
}
