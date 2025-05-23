package com.example.bankcards.dto;

import com.example.bankcards.entity.Status;

import java.math.BigDecimal;
import java.time.YearMonth;

public record CardDtoForAdmin(
        Long id,
        String hiddenNumber,
        Long userId,
        YearMonth expirationDate,
        BigDecimal moneyAmount,
        Status status
) {
}
