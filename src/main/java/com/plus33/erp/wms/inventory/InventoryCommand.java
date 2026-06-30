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
