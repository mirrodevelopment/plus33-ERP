/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.repository
 * File              : WarehouseEventStoreRepository.java
 * Purpose           : JPA Repository providing database CRUD for Wms Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WarehouseEventStoreController
 * Related Service   : WarehouseEventStoreService, WarehouseEventStoreServiceImpl
 * Related Repository: WarehouseEventStoreRepository
 * Related Entity    : WarehouseEventStore
 * Related DTO       : N/A
 * Related Mapper    : WarehouseEventStoreMapper
 * Related DB Table  : warehouse_event_stores
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : WarehouseEventStoreService, WarehouseEventStoreServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Wms Module against the 'warehouse_event_stores' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.WarehouseEventStoreItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code WarehouseEventStoreRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'warehouse_event_stores' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code warehouse_event_stores}</p>
 * <p><b>Module Deps      :</b> Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface WarehouseEventStoreRepository extends JpaRepository<WarehouseEventStoreItem, Long> {
    List<WarehouseEventStoreItem> findByAggregateTypeAndAggregateIdOrderByVersionNumberAsc(String aggregateType, Long aggregateId);
}
