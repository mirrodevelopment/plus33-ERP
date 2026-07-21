/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Auth Module
 * File              : ChangePasswordRequest.java
 * Path              : src/main/java/com/plus33/erp/auth/dto/ChangePasswordRequest.java
 * Purpose           : Carries the authenticated user's current password and desired
 *                     new password for the secure password change endpoint.
 * Version           : 1.0.0
 *
 * Description
 * ---------------------------------------------------------------------------
 * Immutable Java record representing the HTTP request body consumed by
 * AuthController.changePassword() at PUT /api/v1/auth/change-password.
 * Contains two validated fields:
 *
 *   currentPassword — required, non-blank. Verified against the stored
 *                     BCrypt hash using PasswordEncoder.matches() before
 *                     any update is performed. Returns 400 if it does not match.
 *   newPassword     — required, minimum 6 characters (@Size). Encoded with
 *                     BCrypt and persisted to the User entity via UserRepository.
 *
 * Does not expose role-specific logic. Does not interact with the Employee
 * entity. Validation failures produce 400 Bad Request before the controller
 * method body executes. Does not persist to any database table.
 ******************************************************************************/
package com.plus33.erp.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(
        @NotBlank(message = "Current password is required")
        String currentPassword,

        @NotBlank(message = "New password is required")
        @Size(min = 6, message = "New password must be at least 6 characters long")
        String newPassword
) {}
