package com.example.bankcards.service;

import com.example.bankcards.dto.CardBlockRequestDto;
import com.example.bankcards.dto.CardCreateUpdateRequest;
import com.example.bankcards.dto.MoneyDepositRequest;
import com.example.bankcards.dto.MoneyTransferRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardBlockRequest;
import com.example.bankcards.entity.Status;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CardService {
    Card createCard(CardCreateUpdateRequest request);

    Page<Card> searchCards(Authentication authentication, String last4, Status status, Pageable pageable);

    void transferMoney(MoneyTransferRequest request, Authentication authentication);

    Card changeStatus(Long cardId, Status status);

    void deleteCard(Long cardId);

    CardBlockRequest requestBlockCard(CardBlockRequestDto blockRequestDto, Authentication authentication);

    Page<CardBlockRequest> getPendingBlockRequests(Pageable pageable);

    CardBlockRequest processBlockRequest(Long id, boolean approve);

    List<CardBlockRequest> getUsersRequestsForBlock(Authentication authentication);

    Card depositMoney(MoneyDepositRequest depositRequest);
}
