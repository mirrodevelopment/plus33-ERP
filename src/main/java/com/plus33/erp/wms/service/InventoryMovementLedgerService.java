package com.plus33.erp.wms.service;

import com.plus33.erp.wms.entity.*;

import java.math.BigDecimal;
import java.util.List;

public interface InventoryMovementLedgerService {
    InventoryMovement recordMovement(Long companyId, Long warehouseId, String movementType,
                                     Long productId, Long fromLocationId, Long toLocationId,
                                     String lotNumber, String serialNumber, BigDecimal quantity,
                                     Long unitId, BigDecimal unitCost, String sourceType,
                                     Long sourceId, Long sourceLineId, Long performedBy,
                                     String idempotencyKey, String notes);

    List<InventoryMovement> findByProduct(Long companyId, Long productId);
    List<InventoryMovement> findBySourceDocument(String sourceType, Long sourceId);
}
