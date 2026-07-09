/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Hcm Module
 * Package           : com.plus33.erp.hcm.repository
 * File              : HcmEventStoreItemRepository.java
 * Purpose           : JPA Repository providing database CRUD for Hcm Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: HcmEventStoreItemController
 * Related Service   : HcmEventStoreItemService, HcmEventStoreItemServiceImpl
 * Related Repository: HcmEventStoreItemRepository
 * Related Entity    : HcmEventStoreItem
 * Related DTO       : N/A
 * Related Mapper    : HcmEventStoreItemMapper
 * Related DB Table  : hcm_event_store_items
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : HcmEventStoreItemService, HcmEventStoreItemServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Hcm Module against the 'hcm_event_store_items' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.hcm.repository;

import com.plus33.erp.hcm.entity.HcmEventStoreItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HcmEventStoreItemRepository extends JpaRepository<HcmEventStoreItem, Long> {
}
