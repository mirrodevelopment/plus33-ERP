/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.dto
 * File              : PurchaseOrderItemResponse.java
 * Purpose           : Data Transfer Object for request/response in Procurement Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PurchaseOrderItemController
 * Related Service   : PurchaseOrderItemService, PurchaseOrderItemServiceImpl
 * Related Repository: PurchaseOrderItemRepository
 * Related Entity    : PurchaseOrderItem
 * Related DTO       : PurchaseOrderItemResponse
 * Related Mapper    : PurchaseOrderItemMapper
 * Related DB Table  : purchase_order_items
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PurchaseOrderItemController, PurchaseOrderItemService, PurchaseOrderItemServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Procurement Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.procurement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code PurchaseOrderItemResponse}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Procurement Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Schema(description = "Purchase Order line item response details")
public record PurchaseOrderItemResponse(
        @Schema(description = "Database ID of the line item", example = "1")
        Long id,

        @Schema(description = "Product ID", example = "1")
        Long productId,

        @Schema(description = "Product Code", example = "PRD_001")
        String productCode,

        @Schema(description = "Product Name", example = "Whole Milk")
        String productName,

        @Schema(description = "Ordered quantity", example = "100.00")
        BigDecimal orderedQuantity,

        @Schema(description = "Unit price of the product", example = "4.50")
        BigDecimal unitPrice,

        @Schema(description = "Received quantity", example = "0.00")
        BigDecimal receivedQuantity,

        @Schema(description = "Remaining quantity", example = "100.00")
        BigDecimal remainingQuantity,

        @Schema(description = "Line item remarks", example = "Monthly order")
        String remarks,

        @Schema(description = "Budget dimension set ID", example = "1")
        Long dimensionSetId
) {}