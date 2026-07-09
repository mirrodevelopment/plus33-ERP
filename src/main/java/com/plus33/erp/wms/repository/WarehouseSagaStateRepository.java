/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.repository
 * File              : WarehouseSagaStateRepository.java
 * Purpose           : JPA Repository providing database CRUD for Wms Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WarehouseSagaStateController
 * Related Service   : WarehouseSagaStateService, WarehouseSagaStateServiceImpl
 * Related Repository: WarehouseSagaStateRepository
 * Related Entity    : WarehouseSagaState
 * Related DTO       : N/A
 * Related Mapper    : WarehouseSagaStateMapper
 * Related DB Table  : warehouse_saga_states
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : WarehouseSagaStateService, WarehouseSagaStateServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Wms Module against the 'warehouse_saga_states' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.WarehouseSagaState;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code WarehouseSagaStateRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'warehouse_saga_states' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code warehouse_saga_states}</p>
 * <p><b>Module Deps      :</b> Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface WarehouseSagaStateRepository extends JpaRepository<WarehouseSagaState, Long> {
    Optional<WarehouseSagaState> findByCorrelationId(String correlationId);
}
