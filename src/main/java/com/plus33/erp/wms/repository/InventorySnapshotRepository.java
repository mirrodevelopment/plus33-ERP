/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.repository
 * File              : InventorySnapshotRepository.java
 * Purpose           : JPA Repository providing database CRUD for Wms Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventorySnapshotController
 * Related Service   : InventorySnapshotService, InventorySnapshotServiceImpl
 * Related Repository: InventorySnapshotRepository
 * Related Entity    : InventorySnapshot
 * Related DTO       : N/A
 * Related Mapper    : InventorySnapshotMapper
 * Related DB Table  : inventory_snapshots
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : InventorySnapshotService, InventorySnapshotServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Wms Module against the 'inventory_snapshots' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.InventorySnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code InventorySnapshotRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'inventory_snapshots' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code inventory_snapshots}</p>
 * <p><b>Module Deps      :</b> Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface InventorySnapshotRepository extends JpaRepository<InventorySnapshot, Long> {
    List<InventorySnapshot> findByCompanyIdAndWarehouseIdAndSnapshotDate(Long companyId, Long warehouseId, LocalDate snapshotDate);
}
