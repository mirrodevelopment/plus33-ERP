/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.dto
 * File              : InventoryAdjustmentResponse.java
 * Purpose           : Data Transfer Object for request/response in Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryAdjustmentController
 * Related Service   : InventoryAdjustmentService, InventoryAdjustmentServiceImpl
 * Related Repository: InventoryAdjustmentRepository
 * Related Entity    : InventoryAdjustment
 * Related DTO       : InventoryAdjustmentItemResponse, InventoryAdjustmentResponse
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

import com.plus33.erp.inventory.entity.InventoryAdjustmentStatus;
import com.plus33.erp.inventory.entity.InventoryAdjustmentType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code InventoryAdjustmentResponse}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Inventory Module.</p>
 *
 * <p><b>Module Deps      :</b> Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public record InventoryAdjustmentResponse(
        Long id,
        String adjustmentNumber,
        Long companyId,
        Long warehouseId,
        Long storeId,
        InventoryAdjustmentType adjustmentType,
        InventoryAdjustmentStatus status,
        UUID clientReferenceId,
        String remarks,
        Long createdById,
        LocalDateTime createdAt,
        Long submittedById,
        LocalDateTime submittedAt,
        Long approvedById,
        LocalDateTime approvedAt,
        Long postedById,
        LocalDateTime postedAt,
        Long cancelledById,
        LocalDateTime cancelledAt,
        String cancellationReason,
        List<InventoryAdjustmentItemResponse> items,
        Long version
) {}
