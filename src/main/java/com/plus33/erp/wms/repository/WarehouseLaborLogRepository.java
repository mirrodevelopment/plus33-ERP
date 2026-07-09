/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.repository
 * File              : WarehouseLaborLogRepository.java
 * Purpose           : JPA Repository providing database CRUD for Wms Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WarehouseLaborLogController
 * Related Service   : WarehouseLaborLogService, WarehouseLaborLogServiceImpl
 * Related Repository: WarehouseLaborLogRepository
 * Related Entity    : WarehouseLaborLog
 * Related DTO       : N/A
 * Related Mapper    : WarehouseLaborLogMapper
 * Related DB Table  : warehouse_labor_logs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : WarehouseLaborLogService, WarehouseLaborLogServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Wms Module against the 'warehouse_labor_logs' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.WarehouseLaborLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code WarehouseLaborLogRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'warehouse_labor_logs' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code warehouse_labor_logs}</p>
 * <p><b>Module Deps      :</b> Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface WarehouseLaborLogRepository extends JpaRepository<WarehouseLaborLog, Long> {
    List<WarehouseLaborLog> findByCompanyIdAndWarehouseId(Long companyId, Long warehouseId);
    List<WarehouseLaborLog> findByUserId(Long userId);
}
