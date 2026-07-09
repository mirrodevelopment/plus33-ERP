/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.dto
 * File              : InventoryAdjustmentRequest.java
 * Purpose           : Data Transfer Object for request/response in Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryAdjustmentController
 * Related Service   : InventoryAdjustmentService, InventoryAdjustmentServiceImpl
 * Related Repository: InventoryAdjustmentRepository
 * Related Entity    : InventoryAdjustment
 * Related DTO       : InventoryAdjustmentItemRequest, InventoryAdjustmentRequest
 * Related Mapper    : InventoryAdjustmentMapper
 * Related DB Table  : inventory_adjustments
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : InventoryAdjustmentController, InventoryAdjustmentService, InventoryAdjustmentServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Inventory Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.inventory.dto;

import com.plus33.erp.inventory.entity.InventoryAdjustmentType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code InventoryAdjustmentRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Inventory Module.</p>
 *
 * <p><b>Module Deps      :</b> Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public record InventoryAdjustmentRequest(
        @NotNull(message = "Company ID is required")
        Long companyId,

        Long warehouseId,
        Long storeId,

        @NotNull(message = "Adjustment type is required")
        InventoryAdjustmentType adjustmentType,

        @NotNull(message = "Client reference ID is required")
        UUID clientReferenceId,

        String remarks,

        @NotEmpty(message = "Adjustment must contain at least one item")
        @Valid
        List<InventoryAdjustmentItemRequest> items
) {}
