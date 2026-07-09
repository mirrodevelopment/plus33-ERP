/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.dto
 * File              : InventoryTransferItemRequest.java
 * Purpose           : Data Transfer Object for request/response in Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryTransferItemController
 * Related Service   : InventoryTransferItemService, InventoryTransferItemServiceImpl
 * Related Repository: InventoryTransferItemRepository
 * Related Entity    : InventoryTransferItem
 * Related DTO       : InventoryTransferItemRequest
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

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code InventoryTransferItemRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Inventory Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public record InventoryTransferItemRequest(
        @NotNull(message = "Product ID is required")
        Long productId,

        @NotNull(message = "Quantity is required")
        @DecimalMin(value = "0.01", message = "Quantity must be greater than zero")
        BigDecimal quantity
) {}
