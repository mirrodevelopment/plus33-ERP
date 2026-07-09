/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.dto
 * File              : GoodsReceiptItemRequest.java
 * Purpose           : Data Transfer Object for request/response in Procurement Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: GoodsReceiptItemController
 * Related Service   : GoodsReceiptItemService, GoodsReceiptItemServiceImpl
 * Related Repository: GoodsReceiptItemRepository
 * Related Entity    : GoodsReceiptItem
 * Related DTO       : GoodsReceiptItemRequest
 * Related Mapper    : GoodsReceiptItemMapper
 * Related DB Table  : goods_receipt_items
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : GoodsReceiptItemController, GoodsReceiptItemService, GoodsReceiptItemServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Procurement Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.procurement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code GoodsReceiptItemRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Procurement Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Schema(description = "Goods Receipt line item request parameters")
public record GoodsReceiptItemRequest(
        @Schema(description = "Product ID", example = "1")
        @NotNull(message = "Product ID is required")
        Long productId,

        @Schema(description = "Received quantity", example = "10.00")
        @NotNull(message = "Received quantity is required")
        @DecimalMin(value = "0.01", message = "Received quantity must be greater than zero")
        BigDecimal receivedQuantity,

        @Schema(description = "Line item remarks", example = "Delivered in good condition")
        @Size(max = 255, message = "Remarks cannot exceed 255 characters")
        String remarks
) {}