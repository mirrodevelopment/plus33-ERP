/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Auth Module
 * Package           : com.plus33.erp.auth.dto
 * File              : TokenResponse.java
 * Purpose           : Data Transfer Object for request/response in Auth Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TokenController
 * Related Service   : TokenService, TokenServiceImpl
 * Related Repository: TokenRepository
 * Related Entity    : Token
 * Related DTO       : TokenResponse
 * Related Mapper    : TokenMapper
 * Related DB Table  : tokens
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : TokenController, TokenService, TokenServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Auth Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.auth.dto;

public record TokenResponse(

        String accessToken,
        String tokenType,
        long expiresIn
) {}
