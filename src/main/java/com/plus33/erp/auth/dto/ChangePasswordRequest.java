/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Auth Module
 * Package           : com.plus33.erp.auth.dto
 * File              : ChangePasswordRequest.java
 * Purpose           : Data Transfer Object for changing user password
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AuthController
 * Related DTO       : ChangePasswordRequest
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for change password request payload.
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
