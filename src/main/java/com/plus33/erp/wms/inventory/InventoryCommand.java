/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.inventory
 * File              : InventoryCommand.java
 * Purpose           : Component of Wms Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryCommandController
 * Related Service   : InventoryCommandService, InventoryCommandServiceImpl
 * Related Repository: InventoryCommandRepository
 * Related Entity    : InventoryCommand
 * Related DTO       : N/A
 * Related Mapper    : InventoryCommandMapper
 * Related DB Table  : inventory_commands
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Wms Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Wms Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.wms.inventory;

import java.math.BigDecimal;

/**
 * Encapsulates a command requesting an inventory modification through the
 * centralized {@link InventoryEngine} command pipeline.
 */
public record InventoryCommand(
        Long companyId,
        Long warehouseId,
        String movementType,
        Long productId,
        Long fromLocationId,
        Long toLocationId,
        String lotNumber,
        String serialNumber,
        BigDecimal quantity,
        Long unitId,
        BigDecimal unitCost,
        String sourceType,
        Long sourceId,
        Long sourceLineId,
        Long performedBy,
        Long ownerCompanyId,
        String idempotencyKey,
        String notes
) {}
