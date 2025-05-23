package com.example.bankcards.util;

import java.util.Arrays;

public class CardNumberEncrypter {
    public static String encrypt(String cardNumber){
        char[] chars = cardNumber.toCharArray();
        char[] encryptedChars = new char[chars.length];
        for (int i = 0; i < chars.length - 4; i++) {
            encryptedChars[i] = '*';
        }
        for (int i = chars.length - 4; i < chars.length; i++){
            encryptedChars[i] = chars[i];
        }
        return new String(encryptedChars);
    }
}
