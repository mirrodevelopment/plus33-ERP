/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Auth Module
 * File              : LoginRequest.java
 * Path              : src/main/java/com/plus33/erp/auth/dto/LoginRequest.java
 * Purpose           : Carries user email and password credentials submitted to the
 *                     POST /api/v1/auth/login endpoint for JWT token generation.
 * Version           : 1.0.0
 *
 * Description
 * ---------------------------------------------------------------------------
 * Immutable Java record representing the HTTP request body for the login
 * endpoint. Contains two fields validated by Jakarta Bean Validation:
 *
 *   email    — required, must conform to valid email format (@Email).
 *              Used as the primary authentication principal identifier.
 *   password — required, non-blank string used for BCrypt credential
 *              verification via Spring AuthenticationManager.
 *
 * Consumed exclusively by AuthController.login(). Deserialized from
 * application/json request body by Spring MVC. Validation failures
 * result in 400 Bad Request responses before the controller executes.
 * Does not persist to any database table.
 ******************************************************************************/
package com.plus33.erp.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Password is required")
        String password
) {}
