package com.example.bankcards.dto;

import java.math.BigDecimal;

public record MoneyTransferRequest(
        Long sourceCardId,
        Long targetCardId,
        BigDecimal amount
) {
}
