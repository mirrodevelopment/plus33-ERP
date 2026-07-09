/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.dto
 * File              : CustomerInvoiceItemRequest.java
 * Purpose           : Data Transfer Object for request/response in Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CustomerInvoiceItemController
 * Related Service   : CustomerInvoiceItemService, CustomerInvoiceItemServiceImpl
 * Related Repository: CustomerInvoiceItemRepository
 * Related Entity    : CustomerInvoiceItem
 * Related DTO       : CustomerInvoiceItemRequest
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

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code CustomerInvoiceItemRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Sales Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public record CustomerInvoiceItemRequest(
        Long salesOrderItemId,
        Long pickListItemId,

        @NotNull(message = "Product ID is required")
        Long productId,

        @NotNull(message = "Quantity is required")
        @DecimalMin(value = "0.01", message = "Quantity must be greater than zero")
        BigDecimal quantity,

        @NotNull(message = "Unit price is required")
        @DecimalMin(value = "0.00", message = "Unit price cannot be negative")
        BigDecimal unitPrice,

        BigDecimal discountPercentage,
        BigDecimal taxPercentage
) {}
