/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.dto
 * File              : InventoryAdjustmentItemRequest.java
 * Purpose           : Data Transfer Object for request/response in Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryAdjustmentItemController
 * Related Service   : InventoryAdjustmentItemService, InventoryAdjustmentItemServiceImpl
 * Related Repository: InventoryAdjustmentItemRepository
 * Related Entity    : InventoryAdjustmentItem
 * Related DTO       : InventoryAdjustmentItemRequest
 * Related Mapper    : InventoryAdjustmentItemMapper
 * Related DB Table  : inventory_adjustment_items
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : InventoryAdjustmentItemController, InventoryAdjustmentItemService, InventoryAdjustmentItemServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Inventory Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.inventory.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record InventoryAdjustmentItemRequest(
        @NotNull(message = "Product ID is required")
        Long productId,

        @NotNull(message = "Quantity is required")
        BigDecimal quantity
) {}
