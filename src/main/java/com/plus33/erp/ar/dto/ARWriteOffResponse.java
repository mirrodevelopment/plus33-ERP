/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Ar Module
 * Package           : com.plus33.erp.ar.dto
 * File              : ARWriteOffResponse.java
 * Purpose           : Data Transfer Object for request/response in Ar Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ARWriteOffController
 * Related Service   : ARWriteOffService, ARWriteOffServiceImpl
 * Related Repository: ARWriteOffRepository
 * Related Entity    : ARWriteOff
 * Related DTO       : ARWriteOffResponse
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Response payload for an AR write-off record.
 */
public record ARWriteOffResponse(
        Long id,
        String writeOffNumber,
        Long companyId,
        Long customerInvoiceId,
        String invoiceNumber,
        Long customerId,
        String customerName,
        BigDecimal writeOffAmount,
        LocalDate writeOffDate,
        String reason,
        Long journalEntryId,
        String journalEntryNumber,
        Long writtenOffByUserId,
        String writtenOffByUserName,
        LocalDateTime createdAt
) {}
