/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.service.impl
 * File              : InventorySnapshotEngine.java
 * Purpose           : Business logic service layer for Wms Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventorySnapshotEngineController
 * Related Service   : InventorySnapshotEngine
 * Related Repository: InventorySnapshotRepository
 * Related Entity    : InventorySnapshotEngine
 * Related DTO       : N/A
 * Related Mapper    : InventorySnapshotEngineMapper
 * Related DB Table  : inventory_snapshot_engines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : InventorySnapshotEngineController, InventorySnapshotEngineImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Wms Module. Implements InventorySnapshotEngineService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.wms.service.impl;

import com.plus33.erp.wms.entity.InventorySnapshot;
import com.plus33.erp.wms.repository.InventorySnapshotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code InventorySnapshotEngine}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.service.impl}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Wms Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * InventorySnapshotEngineController
 *   --> InventorySnapshotEngine (this)
 *   --> Validate business rules
 *   --> InventorySnapshotEngineRepository (read/write 'inventory_snapshot_engines')
 *   --> InventorySnapshotEngineMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code inventory_snapshot_engines}</p>
 * <p><b>Module Deps      :</b> Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@Transactional
public class InventorySnapshotEngine {

    private final InventorySnapshotRepository snapshotRepo;

    public InventorySnapshotEngine(InventorySnapshotRepository snapshotRepo) {
        this.snapshotRepo = snapshotRepo;
    }

    /**
     * Creates a new daily snapshot and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @return the InventorySnapshot result
     * @throws BusinessException if a business rule is violated
     */
    public InventorySnapshot createDailySnapshot(Long companyId, Long warehouseId, Long locationId, Long productId,
                                                  String lotNumber, BigDecimal qty) {
        InventorySnapshot snapshot = new InventorySnapshot();
        snapshot.setCompanyId(companyId);
        snapshot.setWarehouseId(warehouseId);
        snapshot.setLocationId(locationId);
        snapshot.setProductId(productId);
        snapshot.setLotNumber(lotNumber);
        snapshot.setSnapshotDate(LocalDate.now());
        snapshot.setOnHandQuantity(qty);
        snapshot.setAvailableQuantity(qty);
        return snapshotRepo.save(snapshot);
    }
}