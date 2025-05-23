package com.example.bankcards.service.impl;

import com.example.bankcards.dto.CardBlockRequestDto;
import com.example.bankcards.dto.CardCreateUpdateRequest;
import com.example.bankcards.dto.MoneyTransferRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardBlockRequest;
import com.example.bankcards.entity.Status;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.CardDeleteException;
import com.example.bankcards.exception.MoneyTransferException;
import com.example.bankcards.repository.CardBlockRequestRepository;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.CardNumberGeneratorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceImplTest {

    @Value("${card.generator.periodInMonths}")
    private long period;

    @Mock
    private UserRepository userRepository;
    @Mock
    private CardRepository cardRepository;
    @Mock
    private CardNumberGeneratorService numberGenerator;
    @Mock
    private CardBlockRequestRepository cardBlockRequestRepository;
    @InjectMocks
    private CardServiceImpl cardService;

    @Test
    void createCard() {
        User user = User.builder()
                .id(1L)
                .build();
        CardCreateUpdateRequest request = new CardCreateUpdateRequest(1L);
        String cardNum = "1234567891234567";
        String cardNumAlreadyExists = "alreadyExists";

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(numberGenerator.generateCardNumber())
                .thenReturn(cardNumAlreadyExists, cardNum);
        when(cardRepository.existsByNumber(cardNumAlreadyExists)).thenReturn(true);
        when(cardRepository.existsByNumber(cardNum)).thenReturn(false);

        ArgumentCaptor<Card> captor = ArgumentCaptor.forClass(Card.class);
        when(cardRepository.save(captor.capture())).thenAnswer(invocation -> invocation.getArgument(0));
        Card createdCard = cardService.createCard(request);

        Card card = captor.getValue();
        assertEquals(cardNum, card.getNumber());
        assertEquals(user, card.getUser());
        assertEquals(YearMonth.now().plusMonths(period), card.getExpirationDate());
        assertEquals(Status.ACTIVE, card.getStatus());
        assertEquals(BigDecimal.ZERO, card.getMoneyAmount());
        assertSame(card, createdCard);
    }


    @Test
    void shouldTransferMoney() {
        Card sourceCard = Card.builder()
                .id(1L)
                .moneyAmount(BigDecimal.valueOf(10000))
                .status(Status.ACTIVE)
                .build();
        Card targetCard = Card.builder()
                .id(2L)
                .moneyAmount(BigDecimal.valueOf(100))
                .status(Status.ACTIVE)
                .build();
        User user = User.builder()
                .id(1L)
                .cards(List.of(sourceCard, targetCard))
                .build();
        MoneyTransferRequest request = new MoneyTransferRequest(1L, 2L, BigDecimal.valueOf(5000));

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
        when(cardRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(sourceCard));
        when(cardRepository.findByIdAndUserId(2L, 1L)).thenReturn(Optional.of(targetCard));

        cardService.transferMoney(request, authentication);

        assertEquals(BigDecimal.valueOf(5000), sourceCard.getMoneyAmount());
        assertEquals(BigDecimal.valueOf(5100), targetCard.getMoneyAmount());
    }

    @Test
    void shouldThrowIfNotEnoughMoney() {
        Card sourceCard = Card.builder()
                .id(1L)
                .moneyAmount(BigDecimal.valueOf(4999))
                .status(Status.ACTIVE)
                .build();
        Card targetCard = Card.builder()
                .id(2L)
                .moneyAmount(BigDecimal.valueOf(0))
                .status(Status.ACTIVE)
                .build();
        User user = User.builder()
                .id(1L)
                .cards(List.of(sourceCard, targetCard))
                .build();
        MoneyTransferRequest request = new MoneyTransferRequest(1L, 2L, BigDecimal.valueOf(5000));

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
        when(cardRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(sourceCard));
        when(cardRepository.findByIdAndUserId(2L, 1L)).thenReturn(Optional.of(targetCard));

        MoneyTransferException exception =
                assertThrows(MoneyTransferException.class, () -> cardService.transferMoney(request, authentication));
        assertEquals("Not enough money on source card", exception.getMessage());
    }

    @Test
    void shouldThrowIfCardBlocked() {
        Card sourceCard = Card.builder()
                .id(1L)
                .moneyAmount(BigDecimal.valueOf(10000))
                .status(Status.ACTIVE)
                .build();
        Card targetCard = Card.builder()
                .id(2L)
                .moneyAmount(BigDecimal.valueOf(5000))
                .status(Status.BLOCKED)
                .build();
        User user = User.builder()
                .id(1L)
                .cards(List.of(sourceCard, targetCard))
                .build();
        MoneyTransferRequest request = new MoneyTransferRequest(1L, 2L, BigDecimal.valueOf(5000));

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
        when(cardRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(sourceCard));
        when(cardRepository.findByIdAndUserId(2L, 1L)).thenReturn(Optional.of(targetCard));

        MoneyTransferException exception =
                assertThrows(MoneyTransferException.class, () -> cardService.transferMoney(request, authentication));
        assertEquals("Cannot transfer money because of card status", exception.getMessage());
    }


    @Test
    void shouldUpdateCardStatus() {
        Card card = Card.builder()
                .id(1L)
                .status(Status.ACTIVE)
                .build();
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        when(cardRepository.save(any(Card.class))).thenAnswer(inv -> inv.getArgument(0));

        Card result = cardService.changeStatus(1L, Status.BLOCKED);

        assertEquals(Status.BLOCKED, result.getStatus());
        verify(cardRepository).save(card);
    }

    @Test
    void shouldNotDeleteIfThereIsMoney() {
        Long cardId = 1L;
        Card card = Card.builder()
                .id(cardId)
                .moneyAmount(new BigDecimal(100))
                .build();
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
        CardDeleteException exception = assertThrows(
                CardDeleteException.class,
                () -> cardService.deleteCard(cardId)
        );
        assertEquals("There is some money on this card: " + cardId, exception.getMessage());
        verify(cardRepository, never()).deleteById(any());
    }

    @Test
    void shouldCreateBlockRequest() {
        Long cardId = 10L;
        Long userId = 1L;
        String reason = "Lost card";

        User user = User.builder().id(userId).build();
        Card card = Card.builder().id(cardId).user(user).build();
        CardBlockRequestDto requestDto = new CardBlockRequestDto(cardId, reason);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
        when(cardRepository.findByIdAndUserId(cardId, userId)).thenReturn(Optional.of(card));

        ArgumentCaptor<CardBlockRequest> captor = ArgumentCaptor.forClass(CardBlockRequest.class);
        when(cardBlockRequestRepository.save(captor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

        CardBlockRequest result = cardService.requestBlockCard(requestDto, authentication);

        CardBlockRequest saved = captor.getValue();
        assertEquals(user, saved.getUser());
        assertEquals(card, saved.getCard());
        assertEquals(reason, saved.getReason());
        assertSame(saved, result);
    }

    @Test
    void shouldApproveBlockRequest() {
        Long requestId = 1L;
        boolean approve = true;
        Card card = Card.builder()
                .build();
        CardBlockRequest request = CardBlockRequest.builder()
                .id(requestId)
                .card(card)
                .processed(false)
                .build();
        ArgumentCaptor<CardBlockRequest> captor = ArgumentCaptor.forClass(CardBlockRequest.class);
        when(cardBlockRequestRepository.save(captor.capture())).thenAnswer(invocation -> invocation.getArgument(0));
        when(cardBlockRequestRepository.findById(requestId)).thenReturn(Optional.of(request));
        CardBlockRequest processed = cardService.processBlockRequest(requestId, approve);

        CardBlockRequest saved = captor.getValue();
        assertEquals(true, saved.isProcessed());
        assertEquals(true, saved.isApproved());
        assertEquals(Status.BLOCKED, card.getStatus());
        assertSame(saved, processed);
    }
}