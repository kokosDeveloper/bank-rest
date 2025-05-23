package com.example.bankcards.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardNumberEncrypterTest {
    @Test
    void shouldEncryptCardNumber(){
        String source = "1234567891234567";
        String encrypted = CardNumberEncrypter.encrypt(source);
        String expected = "************4567";
        assertEquals(expected, encrypted);
    }

}