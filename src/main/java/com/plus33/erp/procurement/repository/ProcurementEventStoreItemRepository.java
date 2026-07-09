/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.repository
 * File              : ProcurementEventStoreItemRepository.java
 * Purpose           : JPA Repository providing database CRUD for Procurement Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProcurementEventStoreItemController
 * Related Service   : ProcurementEventStoreItemService, ProcurementEventStoreItemServiceImpl
 * Related Repository: ProcurementEventStoreItemRepository
 * Related Entity    : ProcurementEventStoreItem
 * Related DTO       : N/A
 * Related Mapper    : ProcurementEventStoreItemMapper
 * Related DB Table  : procurement_event_store_items
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProcurementEventStoreItemService, ProcurementEventStoreItemServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Procurement Module against the 'procurement_event_store_items' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.procurement.repository;

import com.plus33.erp.procurement.entity.ProcurementEventStoreItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcurementEventStoreItemRepository extends JpaRepository<ProcurementEventStoreItem, Long> {
}
