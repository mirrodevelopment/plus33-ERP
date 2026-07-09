/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.dto
 * File              : CreditNoteResponse.java
 * Purpose           : Data Transfer Object for request/response in Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CreditNoteController
 * Related Service   : CreditNoteService, CreditNoteServiceImpl
 * Related Repository: CreditNoteRepository
 * Related Entity    : CreditNote
 * Related DTO       : CreditNoteItemResponse, CreditNoteResponse
 * Related Mapper    : CreditNoteMapper
 * Related DB Table  : credit_notes
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CreditNoteController, CreditNoteService, CreditNoteServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Sales Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.sales.dto;

import com.plus33.erp.sales.entity.CreditNoteStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code CreditNoteResponse}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Sales Module.</p>
 *
 * <p><b>Module Deps      :</b> Sales</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public record CreditNoteResponse(
    Long id,
    Long companyId,
    Long customerId,
    String customerName,
    String customerCode,
    Long customerReturnId,
    String customerReturnNumber,
    Long customerInvoiceId,
    String customerInvoiceNumber,
    String creditNoteNumber,
    java.util.UUID clientReferenceId,
    CreditNoteStatus status,
    BigDecimal subtotalAmount,
    BigDecimal taxAmount,
    BigDecimal discountAmount,
    BigDecimal totalAmount,
    Long journalEntryId,
    String journalEntryNumber,
    String remarks,
    Long createdById,
    String createdByName,
    Long approvedById,
    String approvedByName,
    Long cancelledById,
    String cancelledByName,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    LocalDateTime approvedAt,
    LocalDateTime cancelledAt,
    String cancellationReason,
    List<CreditNoteItemResponse> items,
    Long version
) {}
