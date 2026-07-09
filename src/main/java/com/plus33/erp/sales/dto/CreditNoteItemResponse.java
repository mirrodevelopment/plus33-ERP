/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.dto
 * File              : CreditNoteItemResponse.java
 * Purpose           : Data Transfer Object for request/response in Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CreditNoteItemController
 * Related Service   : CreditNoteItemService, CreditNoteItemServiceImpl
 * Related Repository: CreditNoteItemRepository
 * Related Entity    : CreditNoteItem
 * Related DTO       : CreditNoteItemResponse
 * Related Mapper    : CreditNoteItemMapper
 * Related DB Table  : credit_note_items
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CreditNoteItemController, CreditNoteItemService, CreditNoteItemServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Sales Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.sales.dto;

import java.math.BigDecimal;

public record CreditNoteItemResponse(
    Long id,
    Long productId,
    String productName,
    String productCode,
    BigDecimal quantity,
    BigDecimal unitPrice,
    BigDecimal discountPercentage,
    BigDecimal taxPercentage,
    BigDecimal netAmount,
    BigDecimal taxAmount,
    BigDecimal discountAmount,
    BigDecimal totalAmount
) {}
