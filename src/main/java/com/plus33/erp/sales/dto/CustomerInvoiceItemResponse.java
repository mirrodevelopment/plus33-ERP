/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.dto
 * File              : CustomerInvoiceItemResponse.java
 * Purpose           : Data Transfer Object for request/response in Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CustomerInvoiceItemController
 * Related Service   : CustomerInvoiceItemService, CustomerInvoiceItemServiceImpl
 * Related Repository: CustomerInvoiceItemRepository
 * Related Entity    : CustomerInvoiceItem
 * Related DTO       : CustomerInvoiceItemResponse
 * Related Mapper    : CustomerInvoiceItemMapper
 * Related DB Table  : customer_invoice_items
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CustomerInvoiceItemController, CustomerInvoiceItemService, CustomerInvoiceItemServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Sales Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.sales.dto;

import java.math.BigDecimal;

public record CustomerInvoiceItemResponse(
        Long id,
        Long salesOrderItemId,
        Long pickListItemId,
        Long productId,
        String productName,
        String productSku,
        BigDecimal quantity,
        BigDecimal unitPrice,
        BigDecimal discountPercentage,
        BigDecimal taxPercentage,
        BigDecimal netAmount,
        BigDecimal taxAmount,
        BigDecimal discountAmount,
        BigDecimal totalAmount,
        Long version
) {}
