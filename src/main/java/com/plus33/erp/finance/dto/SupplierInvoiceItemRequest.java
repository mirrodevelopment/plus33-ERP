/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.dto
 * File              : SupplierInvoiceItemRequest.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SupplierInvoiceItemController
 * Related Service   : SupplierInvoiceItemService, SupplierInvoiceItemServiceImpl
 * Related Repository: SupplierInvoiceItemRepository
 * Related Entity    : SupplierInvoiceItem
 * Related DTO       : SupplierInvoiceItemRequest
 * Related Mapper    : SupplierInvoiceItemMapper
 * Related DB Table  : supplier_invoice_items
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : SupplierInvoiceItemController, SupplierInvoiceItemService, SupplierInvoiceItemServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code SupplierInvoiceItemRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.dto}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Finance Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierInvoiceItemRequest {

    @NotNull(message = "Purchase order item ID is required")
    private Long purchaseOrderItemId;

    private Long goodsReceiptItemId;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than zero")
    private BigDecimal quantity;

    @NotNull(message = "Unit price is required")
    @DecimalMin(value = "0.00", message = "Unit price cannot be negative")
    private BigDecimal unitPrice;

    @NotNull(message = "Tax rate is required")
    @DecimalMin(value = "0.00", message = "Tax rate cannot be negative")
    private BigDecimal taxRate;

    @NotNull(message = "Discount amount is required")
    @DecimalMin(value = "0.00", message = "Discount amount cannot be negative")
    private BigDecimal discountAmount;

    private Long dimensionSetId;
}