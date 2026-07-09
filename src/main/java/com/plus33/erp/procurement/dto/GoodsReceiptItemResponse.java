/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.dto
 * File              : GoodsReceiptItemResponse.java
 * Purpose           : Data Transfer Object for request/response in Procurement Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: GoodsReceiptItemController
 * Related Service   : GoodsReceiptItemService, GoodsReceiptItemServiceImpl
 * Related Repository: GoodsReceiptItemRepository
 * Related Entity    : GoodsReceiptItem
 * Related DTO       : GoodsReceiptItemResponse
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
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code GoodsReceiptItemResponse}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Procurement Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Schema(description = "Goods Receipt line item response details")
public record GoodsReceiptItemResponse(
        @Schema(description = "Database ID of the line item", example = "1")
        Long id,

        @Schema(description = "Product ID", example = "1")
        Long productId,

        @Schema(description = "Product SKU / Code", example = "RAW-MLK-001")
        String productCode,

        @Schema(description = "Product Name", example = "Milk")
        String productName,

        @Schema(description = "Received quantity", example = "10.00")
        BigDecimal receivedQuantity,

        @Schema(description = "Line item remarks", example = "Delivered in good condition")
        String remarks
) {}