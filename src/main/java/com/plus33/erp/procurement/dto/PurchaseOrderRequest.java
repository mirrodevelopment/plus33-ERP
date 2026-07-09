/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.dto
 * File              : PurchaseOrderRequest.java
 * Purpose           : Data Transfer Object for request/response in Procurement Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PurchaseOrderController
 * Related Service   : PurchaseOrderService, PurchaseOrderServiceImpl
 * Related Repository: PurchaseOrderRepository
 * Related Entity    : PurchaseOrder
 * Related DTO       : PurchaseOrderItemRequest, PurchaseOrderRequest
 * Related Mapper    : PurchaseOrderMapper
 * Related DB Table  : purchase_orders
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PurchaseOrderController, PurchaseOrderService, PurchaseOrderServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Procurement Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.procurement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code PurchaseOrderRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Procurement Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Schema(description = "Purchase Order creation/update parameters")
public record PurchaseOrderRequest(
        @Schema(description = "Company ID mapping", example = "1")
        @NotNull(message = "Company ID is required")
        Long companyId,

        @Schema(description = "Supplier ID mapping", example = "1")
        @NotNull(message = "Supplier ID is required")
        Long supplierId,

        @Schema(description = "Associated Purchase Request ID if mapping", example = "1")
        Long purchaseRequestId,

        @Schema(description = "Expected delivery date", example = "2026-07-01")
        @NotNull(message = "Expected delivery date is required")
        @FutureOrPresent(message = "Expected delivery date cannot be in the past")
        LocalDate expectedDeliveryDate,

        @Schema(description = "Header level notes", example = "Approved replenishment order")
        String notes,

        @Schema(description = "Header level discount amount", example = "10.00")
        @DecimalMin(value = "0.00", message = "Discount amount must be greater than or equal to zero")
        BigDecimal discountAmount,

        @Schema(description = "Header level tax amount", example = "15.00")
        @DecimalMin(value = "0.00", message = "Tax amount must be greater than or equal to zero")
        BigDecimal taxAmount,

        @Schema(description = "Currency code of the transaction", example = "AED")
        @Size(max = 10, message = "Currency code cannot exceed 10 characters")
        String currencyCode,

        @Schema(description = "List of line items")
        @NotEmpty(message = "Purchase order must contain at least one line item")
        @Valid
        List<PurchaseOrderItemRequest> items
) {}