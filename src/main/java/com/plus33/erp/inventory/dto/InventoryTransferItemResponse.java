/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.dto
 * File              : InventoryTransferItemResponse.java
 * Purpose           : Data Transfer Object for request/response in Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryTransferItemController
 * Related Service   : InventoryTransferItemService, InventoryTransferItemServiceImpl
 * Related Repository: InventoryTransferItemRepository
 * Related Entity    : InventoryTransferItem
 * Related DTO       : InventoryTransferItemResponse
 * Related Mapper    : InventoryTransferItemMapper
 * Related DB Table  : inventory_transfer_items
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : InventoryTransferItemController, InventoryTransferItemService, InventoryTransferItemServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Inventory Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.inventory.dto;

import java.math.BigDecimal;

public record InventoryTransferItemResponse(
        Long id,
        Long productId,
        String productName,
        BigDecimal quantity,
        BigDecimal receivedQuantity,
        BigDecimal remainingQuantity
) {}
