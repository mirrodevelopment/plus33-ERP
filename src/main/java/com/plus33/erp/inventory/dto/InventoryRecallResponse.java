/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.dto
 * File              : InventoryRecallResponse.java
 * Purpose           : Data Transfer Object for request/response in Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryRecallController
 * Related Service   : InventoryRecallService, InventoryRecallServiceImpl
 * Related Repository: InventoryRecallRepository
 * Related Entity    : InventoryRecall
 * Related DTO       : InventoryRecallResponse
 * Related Mapper    : InventoryRecallMapper
 * Related DB Table  : inventory_recalls
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : InventoryRecallController, InventoryRecallService, InventoryRecallServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Inventory Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.inventory.dto;

import com.plus33.erp.inventory.entity.InventoryRecallStatus;
import java.time.LocalDateTime;

public record InventoryRecallResponse(
        Long id,
        Long companyId,
        Long productId,
        Long lotId,
        String lotNumber,
        Long serialId,
        String serialNumber,
        String recallNumber,
        String recallReason,
        String recallReferenceNumber,
        InventoryRecallStatus status,
        Long recalledById,
        LocalDateTime recalledAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
