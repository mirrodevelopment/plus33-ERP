/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.service
 * File              : CreditNoteService.java
 * Purpose           : Service interface contract defining the API for Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CreditNoteController
 * Related Service   : CreditNoteService, CreditNoteServiceImpl
 * Related Repository: CreditNoteRepository
 * Related Entity    : CreditNote
 * Related DTO       : CreditNoteResponse, PageResponse
 * Related Mapper    : CreditNoteMapper
 * Related DB Table  : credit_notes
 * Related REST APIs : N/A
 * Depends On        : Common Module
 * Used By           : Sales Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Sales Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.sales.service;

import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.sales.dto.CreditNoteResponse;
import org.springframework.data.domain.Pageable;

/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code CreditNoteService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.service}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Sales Module.</p>
 *
 * <p><b>Module Deps      :</b> Common, Sales</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface CreditNoteService {
    CreditNoteResponse getCreditNoteById(Long id);
    PageResponse<CreditNoteResponse> searchCreditNotes(Long companyId, Long customerId, String creditNoteNumber, String status, Pageable pageable);
}
