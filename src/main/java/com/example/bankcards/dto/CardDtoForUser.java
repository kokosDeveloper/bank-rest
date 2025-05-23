package com.example.bankcards.dto;

import com.example.bankcards.entity.Status;

import java.math.BigDecimal;
import java.time.YearMonth;

public record CardDtoForUser(
        Long id,
        String hiddenNumber,
        YearMonth expirationDate,
        BigDecimal moneyAmount,
        Status status
) {
}
