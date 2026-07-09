/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Ar Module
 * Package           : com.plus33.erp.ar.dto
 * File              : ARWriteOffRequest.java
 * Purpose           : Data Transfer Object for request/response in Ar Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ARWriteOffController
 * Related Service   : ARWriteOffService, ARWriteOffServiceImpl
 * Related Repository: ARWriteOffRepository
 * Related Entity    : ARWriteOff
 * Related DTO       : ARWriteOffRequest
 * Related Mapper    : ARWriteOffMapper
 * Related DB Table  : a_r_write_offs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ARWriteOffController, ARWriteOffService, ARWriteOffServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Ar Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.ar.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Request payload for recording a bad-debt write-off.
 */
public record ARWriteOffRequest(

        @NotNull(message = "Company ID is required")
        Long companyId,

        @NotNull(message = "Customer Invoice ID is required")
        Long customerInvoiceId,

        @NotNull(message = "Write-off amount is required")
        @Positive(message = "Write-off amount must be greater than zero")
        BigDecimal writeOffAmount,

        @NotNull(message = "Write-off date is required")
        LocalDate writeOffDate,

        String reason
) {}
