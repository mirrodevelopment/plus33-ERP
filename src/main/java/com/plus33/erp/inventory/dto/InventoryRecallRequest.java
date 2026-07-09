/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.dto
 * File              : InventoryRecallRequest.java
 * Purpose           : Data Transfer Object for request/response in Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryRecallController
 * Related Service   : InventoryRecallService, InventoryRecallServiceImpl
 * Related Repository: InventoryRecallRepository
 * Related Entity    : InventoryRecall
 * Related DTO       : InventoryRecallRequest
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

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InventoryRecallRequest(
        @NotNull(message = "Company ID is required")
        Long companyId,

        @NotNull(message = "Product ID is required")
        Long productId,

        Long lotId,

        Long serialId,

        @NotBlank(message = "Recall reason is required")
        String recallReason,

        String recallReferenceNumber
) {}
