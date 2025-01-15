package com.nnkd.managementbe.utils;

import java.util.Base64;

public class TokenUtils {
    public static String encodeToken(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes());
    }

    public static String decodeToken(String token) {
        byte[] decodedBytes = Base64.getDecoder().decode(token);
        return new String(decodedBytes);
    }
}
