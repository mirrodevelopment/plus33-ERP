/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.service
 * File              : InventoryMovementLedgerService.java
 * Purpose           : Service interface contract defining the API for Wms Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryMovementLedgerController
 * Related Service   : InventoryMovementLedgerService, InventoryMovementLedgerServiceImpl
 * Related Repository: InventoryMovementLedgerRepository
 * Related Entity    : InventoryMovementLedger
 * Related DTO       : N/A
 * Related Mapper    : InventoryMovementLedgerMapper
 * Related DB Table  : inventory_movement_ledgers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Wms Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Wms Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.wms.service;

import com.plus33.erp.wms.entity.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code InventoryMovementLedgerService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.service}</p>
 * <p><b>Layer  :</b> Component of Wms Module in the PLUS33 Coffee ERP platform.</p>
 *
 * <p><b>Module Deps      :</b> Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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
