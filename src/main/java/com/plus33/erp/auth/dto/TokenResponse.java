package com.plus33.erp.auth.dto;

public record TokenResponse(

        String accessToken,
        String tokenType,
        long expiresIn
) {}
