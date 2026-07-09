/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.repository
 * File              : InventoryTraceEventRepository.java
 * Purpose           : JPA Repository providing database CRUD for Inventory Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryTraceEventController
 * Related Service   : InventoryTraceEventService, InventoryTraceEventServiceImpl
 * Related Repository: InventoryTraceEventRepository
 * Related Entity    : InventoryTraceEvent
 * Related DTO       : N/A
 * Related Mapper    : InventoryTraceEventMapper
 * Related DB Table  : inventory_trace_events
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : InventoryTraceEventService, InventoryTraceEventServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Inventory Module against the 'inventory_trace_events' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.inventory.repository;

import com.plus33.erp.inventory.entity.InventoryTraceEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code InventoryTraceEventRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'inventory_trace_events' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code inventory_trace_events}</p>
 * <p><b>Module Deps      :</b> Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface InventoryTraceEventRepository extends JpaRepository<InventoryTraceEvent, Long>, JpaSpecificationExecutor<InventoryTraceEvent> {

    List<InventoryTraceEvent> findByProductIdOrderByCreatedAtDesc(Long productId);

    List<InventoryTraceEvent> findByLotLotNumberOrderByCreatedAtDesc(String lotNumber);

    List<InventoryTraceEvent> findBySerialSerialNumberOrderByCreatedAtDesc(String serialNumber);
}
