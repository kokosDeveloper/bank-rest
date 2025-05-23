package com.example.bankcards.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record MoneyDepositRequest(
        @NotNull(message = "Card id to deposit is required")
        Long cardId,
        @NotNull(message = "Money amount to deposit is required")
        BigDecimal moneyAmount
) {
}
