package com.example.bankcards.service.impl;

import com.example.bankcards.service.CardNumberGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class CardNumberGeneratorServiceImpl implements CardNumberGeneratorService {
    @Value("${card.generator.bin}")
    private String bin;
    private final Random random;
    @Override
    public String generateCardNumber() {
        StringBuilder cardNumber = new StringBuilder(bin);
        while (cardNumber.length() < 15)
            cardNumber.append(random.nextInt(10));
        int checkDigit = calculateLuhnCheckDigit(cardNumber.toString());
        cardNumber.append(checkDigit);
        return cardNumber.toString();
    }

    private int calculateLuhnCheckDigit(String numberWithoutCheckDigit) {
        int sum = 0;
        for (int i = 0; i < numberWithoutCheckDigit.length(); i++) {
            int digit = Character.getNumericValue(numberWithoutCheckDigit.charAt(i));
            if ((i % 2) == 0) {
                digit *= 2;
                if (digit > 9) digit -= 9;
            }
            sum += digit;
        }
        return (10 - (sum % 10)) % 10;
    }
}
