/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Ar Module
 * Package           : com.plus33.erp.ar.dto
 * File              : CustomerStatementResponse.java
 * Purpose           : Data Transfer Object for request/response in Ar Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CustomerStatementController
 * Related Service   : CustomerStatementService, CustomerStatementServiceImpl
 * Related Repository: CustomerStatementRepository
 * Related Entity    : CustomerStatement
 * Related DTO       : CustomerStatementResponse
 * Related Mapper    : CustomerStatementMapper
 * Related DB Table  : customer_statements
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CustomerStatementController, CustomerStatementService, CustomerStatementServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Ar Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.ar.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Chronological customer AR statement with a running balance after every entry.
 */
public record CustomerStatementResponse(
        Long customerId,
        String customerName,
        Long companyId,
        LocalDate fromDate,
        LocalDate toDate,
        BigDecimal openingBalance,
        BigDecimal closingBalance,
        List<CustomerStatementEntry> entries
) {}
