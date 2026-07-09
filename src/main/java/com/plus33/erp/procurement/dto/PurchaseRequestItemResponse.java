/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.dto
 * File              : PurchaseRequestItemResponse.java
 * Purpose           : Data Transfer Object for request/response in Procurement Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PurchaseRequestItemController
 * Related Service   : PurchaseRequestItemService, PurchaseRequestItemServiceImpl
 * Related Repository: PurchaseRequestItemRepository
 * Related Entity    : PurchaseRequestItem
 * Related DTO       : PurchaseRequestItemResponse
 * Related Mapper    : PurchaseRequestItemMapper
 * Related DB Table  : purchase_request_items
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PurchaseRequestItemController, PurchaseRequestItemService, PurchaseRequestItemServiceImpl
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
 * <p><b>Class  :</b> {@code PurchaseRequestItemResponse}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Procurement Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Schema(description = "Purchase Request line item response details")
public record PurchaseRequestItemResponse(
        @Schema(description = "Database ID of the line item", example = "1")
        Long id,

        @Schema(description = "Product ID", example = "1")
        Long productId,

        @Schema(description = "Product Code", example = "PRD_001")
        String productCode,

        @Schema(description = "Product Name", example = "Whole Milk")
        String productName,

        @Schema(description = "Requested quantity", example = "100.00")
        BigDecimal requestedQuantity,

        @Schema(description = "Approved quantity", example = "100.00")
        BigDecimal approvedQuantity,

        @Schema(description = "Unit of measure", example = "PCS")
        String unitOfMeasure,

        @Schema(description = "Line item remarks", example = "Urgent requirement")
        String remarks,

        @Schema(description = "Budget dimension set ID", example = "1")
        Long dimensionSetId
) {}