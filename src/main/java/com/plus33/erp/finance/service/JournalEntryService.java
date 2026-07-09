/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.service
 * File              : JournalEntryService.java
 * Purpose           : Service interface contract defining the API for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: JournalEntryController
 * Related Service   : JournalEntryService, JournalEntryServiceImpl
 * Related Repository: JournalEntryRepository
 * Related Entity    : JournalEntry
 * Related DTO       : JournalEntryRequest, JournalEntryResponse
 * Related Mapper    : JournalEntryMapper
 * Related DB Table  : journal_entrys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.finance.service;

import com.plus33.erp.finance.dto.JournalEntryRequest;
import com.plus33.erp.finance.dto.JournalEntryResponse;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code JournalEntryService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.service}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Finance Module.</p>
 *
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface JournalEntryService {
    JournalEntryResponse createJournalEntry(JournalEntryRequest request);
    JournalEntryResponse postJournalEntry(Long id);
    JournalEntryResponse getJournalEntryById(Long id);
    List<JournalEntryResponse> getJournalEntriesByCompany(Long companyId);
}
