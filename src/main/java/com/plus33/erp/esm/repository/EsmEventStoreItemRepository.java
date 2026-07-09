/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Esm Module
 * Package           : com.plus33.erp.esm.repository
 * File              : EsmEventStoreItemRepository.java
 * Purpose           : JPA Repository providing database CRUD for Esm Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EsmEventStoreItemController
 * Related Service   : EsmEventStoreItemService, EsmEventStoreItemServiceImpl
 * Related Repository: EsmEventStoreItemRepository
 * Related Entity    : EsmEventStoreItem
 * Related DTO       : N/A
 * Related Mapper    : EsmEventStoreItemMapper
 * Related DB Table  : esm_event_store_items
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : EsmEventStoreItemService, EsmEventStoreItemServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Esm Module against the 'esm_event_store_items' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.esm.repository;

import com.plus33.erp.esm.entity.EsmEventStoreItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EsmEventStoreItemRepository extends JpaRepository<EsmEventStoreItem, Long> {
}
