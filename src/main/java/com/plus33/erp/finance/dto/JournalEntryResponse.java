/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.dto
 * File              : JournalEntryResponse.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: JournalEntryController
 * Related Service   : JournalEntryService, JournalEntryServiceImpl
 * Related Repository: JournalEntryRepository
 * Related Entity    : JournalEntry
 * Related DTO       : JournalEntryLineResponse, JournalEntryResponse
 * Related Mapper    : JournalEntryMapper
 * Related DB Table  : journal_entrys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : JournalEntryController, JournalEntryService, JournalEntryServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code JournalEntryResponse}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Finance Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public record JournalEntryResponse(
        Long id,
        String entryNumber,
        Long companyId,
        LocalDate entryDate,
        String description,
        String sourceModule,
        String sourceReference,
        String status,
        String currencyCode,
        LocalDateTime postedAt,
        List<JournalEntryLineResponse> lines
) {}
