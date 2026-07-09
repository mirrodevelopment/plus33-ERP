/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.dto
 * File              : ProjectedJournalEntry.java
 * Purpose           : Component of Finance Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProjectedJournalEntryController
 * Related Service   : ProjectedJournalEntryService, ProjectedJournalEntryServiceImpl
 * Related Repository: ProjectedJournalEntryRepository
 * Related Entity    : ProjectedJournalEntry
 * Related DTO       : N/A
 * Related Mapper    : ProjectedJournalEntryMapper
 * Related DB Table  : projected_journal_entrys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.finance.assets.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code ProjectedJournalEntry}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.assets.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Finance Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public record ProjectedJournalEntry(
    String entryNumber,
    LocalDate entryDate,
    String description,
    BigDecimal totalAmount,
    List<ProjectedJournalEntryLine> lines
) {}
