package com.example.bankcards.service.impl;

import com.example.bankcards.dto.CardBlockRequestDto;
import com.example.bankcards.dto.CardCreateUpdateRequest;
import com.example.bankcards.dto.MoneyDepositRequest;
import com.example.bankcards.dto.MoneyTransferRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardBlockRequest;
import com.example.bankcards.entity.Status;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.AlreadyProcessedException;
import com.example.bankcards.exception.CardDeleteException;
import com.example.bankcards.exception.MoneyTransferException;
import com.example.bankcards.repository.CardBlockRequestRepository;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.repository.specification.CardSpecification;
import com.example.bankcards.service.CardNumberGeneratorService;
import com.example.bankcards.service.CardService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final CardNumberGeneratorService numberGenerator;
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardBlockRequestRepository cardBlockRequestRepository;
    @Value("${card.generator.periodInMonths}")
    private long period;
    @Override
    public Card createCard(CardCreateUpdateRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() ->
                        new EntityNotFoundException("User with the provided id not found: " + request.userId()));
        String cardNumber;
        do{
            cardNumber = numberGenerator.generateCardNumber();
        }while (cardRepository.existsByNumber(cardNumber));
        Card createdCard = Card.builder()
                .number(cardNumber)
                .user(user)
                .expirationDate(YearMonth.now().plusMonths(period))
                .status(Status.ACTIVE)
                .moneyAmount(BigDecimal.ZERO)
                .build();
        return cardRepository.save(createdCard);
    }


    @Override
    public Page<Card> searchCards(Authentication authentication, String last4, Status status, Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        Specification<Card> spec = Specification
                .where(CardSpecification.hasUserId(user.getId()))
                .and(CardSpecification.hasLastFourDigits(last4))
                .and(CardSpecification.hasStatus(status));
        return cardRepository.findAll(spec, pageable);
    }

    @Transactional
    @Override
    public void transferMoney(MoneyTransferRequest request, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Card sourceCard = cardRepository.findByIdAndUserId(request.sourceCardId(), user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Source card with the provided id not found: " + request.sourceCardId()));
        Card targetCard = cardRepository.findByIdAndUserId(request.targetCardId(), user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Target card with the provided id not found: " + request.targetCardId()));
        if (sourceCard.getStatus() != Status.ACTIVE || targetCard.getStatus() != Status.ACTIVE)
            throw new MoneyTransferException("Cannot transfer money because of card status");
        if (request.amount().compareTo(sourceCard.getMoneyAmount()) > 0){
            throw new MoneyTransferException("Not enough money on source card");
        }
        sourceCard.setMoneyAmount(sourceCard.getMoneyAmount().subtract(request.amount()));
        targetCard.setMoneyAmount(targetCard.getMoneyAmount().add(request.amount()));
    }

    @Transactional
    @Override
    public Card changeStatus(Long cardId, Status status) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("Card with the provided id not found: " + cardId));
        card.setStatus(status);
        return cardRepository.save(card);
    }

    @Override
    public void deleteCard(Long cardId) {
        cardRepository.findById(cardId)
                        .ifPresent(c -> {
                            if (c.getMoneyAmount().compareTo(BigDecimal.ZERO) > 0){
                                throw new CardDeleteException("There is some money on this card: " + c.getId());
                            }
                        });
        cardRepository.deleteById(cardId);
    }

    @Override
    public CardBlockRequest requestBlockCard(CardBlockRequestDto blockRequestDto, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Card card = cardRepository.findByIdAndUserId(blockRequestDto.cardId(), user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Card with the provided id not found: " + blockRequestDto.cardId()));

        CardBlockRequest blockRequest = CardBlockRequest.builder()
                .card(card)
                .user(user)
                .reason(blockRequestDto.reason())
                .build();
        return cardBlockRequestRepository.save(blockRequest);
    }

    @Override
    public Page<CardBlockRequest> getPendingBlockRequests(Pageable pageable) {
        return cardBlockRequestRepository.findByProcessedFalse(pageable);
    }

    @Transactional
    @Override
    public CardBlockRequest processBlockRequest(Long id, boolean approve) {
        CardBlockRequest blockRequest = cardBlockRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Block request with the provided id not found: " + id));
        Card card = blockRequest.getCard();
        if (blockRequest.isProcessed())
            throw new AlreadyProcessedException("Request with the provided id is already processed: " + id);
        if (approve) {
            blockRequest.setProcessed(true);
            blockRequest.setApproved(true);
            card.setStatus(Status.BLOCKED);
        }else {
            blockRequest.setProcessed(true);
            blockRequest.setApproved(false);
        }
        return cardBlockRequestRepository.save(blockRequest);
    }

    @Override
    public List<CardBlockRequest> getUsersRequestsForBlock(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return cardBlockRequestRepository.findByUserId(user.getId());
    }

    @Transactional
    @Override
    public Card depositMoney(MoneyDepositRequest depositRequest) {
        Card card = cardRepository.findById(depositRequest.cardId())
                .orElseThrow(() -> new EntityNotFoundException("Card with the provided id not found: " + depositRequest.cardId()));
        if (card.getStatus() != Status.ACTIVE){
            throw new MoneyTransferException("Cannot deposit, because of status");
        }
        card.setMoneyAmount(card.getMoneyAmount().add(depositRequest.moneyAmount()));
        return cardRepository.save(card);
    }
}
