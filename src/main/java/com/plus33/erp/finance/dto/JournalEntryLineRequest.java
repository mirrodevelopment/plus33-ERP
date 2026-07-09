/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.dto
 * File              : JournalEntryLineRequest.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: JournalEntryLineController
 * Related Service   : JournalEntryLineService, JournalEntryLineServiceImpl
 * Related Repository: JournalEntryLineRepository
 * Related Entity    : JournalEntryLine
 * Related DTO       : JournalEntryLineRequest
 * Related Mapper    : JournalEntryLineMapper
 * Related DB Table  : journal_entry_lines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : JournalEntryLineController, JournalEntryLineService, JournalEntryLineServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code JournalEntryLineRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Finance Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public record JournalEntryLineRequest(
        @NotNull(message = "Account ID is required")
        Long accountId,

        @NotNull(message = "Debit amount is required")
        @PositiveOrZero(message = "Debit amount must be positive or zero")
        BigDecimal debitAmount,

        @NotNull(message = "Credit amount is required")
        @PositiveOrZero(message = "Credit amount must be positive or zero")
        BigDecimal creditAmount,

        Long dimensionSetId
) {}
