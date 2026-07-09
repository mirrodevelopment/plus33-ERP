/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.reporting.dto
 * File              : FiscalYearCloseResponse.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: FiscalYearCloseController
 * Related Service   : FiscalYearCloseService, FiscalYearCloseServiceImpl
 * Related Repository: FiscalYearCloseRepository
 * Related Entity    : FiscalYearClose
 * Related DTO       : FiscalYearCloseResponse
 * Related Mapper    : FiscalYearCloseMapper
 * Related DB Table  : fiscal_year_closes
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : FiscalYearCloseController, FiscalYearCloseService, FiscalYearCloseServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.reporting.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record FiscalYearCloseResponse(
    Long id,
    Long companyId,
    Integer fiscalYear,
    LocalDate startDate,
    LocalDate endDate,
    String status,
    Long closingJournalId,
    String closedBy,
    LocalDateTime closedAt
) {}
