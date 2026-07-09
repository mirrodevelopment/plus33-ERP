/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Auth Module
 * Package           : com.plus33.erp.auth.dto
 * File              : LoginRequest.java
 * Purpose           : Data Transfer Object for request/response in Auth Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: LoginController
 * Related Service   : LoginService, LoginServiceImpl
 * Related Repository: LoginRepository
 * Related Entity    : Login
 * Related DTO       : LoginRequest
 * Related Mapper    : LoginMapper
 * Related DB Table  : logins
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : LoginController, LoginService, LoginServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Auth Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
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
