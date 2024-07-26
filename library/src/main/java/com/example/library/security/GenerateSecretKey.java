package com.example.library.security;

import io.jsonwebtoken.security.Keys;

public class GenerateSecretKey {
    public static void main(String[] args) {
        var key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS512);
        String secretKey = java.util.Base64.getEncoder().encodeToString(key.getEncoded());
        System.out.println(secretKey);
    }
}
