/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.dto
 * File              : PurchaseRequestItemRequest.java
 * Purpose           : Data Transfer Object for request/response in Procurement Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PurchaseRequestItemController
 * Related Service   : PurchaseRequestItemService, PurchaseRequestItemServiceImpl
 * Related Repository: PurchaseRequestItemRepository
 * Related Entity    : PurchaseRequestItem
 * Related DTO       : PurchaseRequestItemRequest
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
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code PurchaseRequestItemRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Procurement Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Schema(description = "Purchase Request line item request parameters")
public record PurchaseRequestItemRequest(
        @Schema(description = "Product ID", example = "1")
        @NotNull(message = "Product ID is required")
        Long productId,

        @Schema(description = "Requested quantity", example = "100.00")
        @NotNull(message = "Requested quantity is required")
        @DecimalMin(value = "0.01", message = "Requested quantity must be greater than zero")
        BigDecimal requestedQuantity,

        @Schema(description = "Unit of measure (e.g. PCS, KG)", example = "PCS")
        @Size(max = 50, message = "Unit of measure cannot exceed 50 characters")
        String unitOfMeasure,

        @Schema(description = "Line item remarks", example = "Urgent requirement")
        @Size(max = 255, message = "Remarks cannot exceed 255 characters")
        String remarks,

        @Schema(description = "Budget dimension set ID", example = "1")
        Long dimensionSetId
) {
    public PurchaseRequestItemRequest(Long productId, BigDecimal requestedQuantity, String unitOfMeasure, String remarks) {
        this(productId, requestedQuantity, unitOfMeasure, remarks, null);
    }
}