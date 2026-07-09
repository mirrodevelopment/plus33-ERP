/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.repository
 * File              : BiEventStoreRepository.java
 * Purpose           : JPA Repository providing database CRUD for Bi Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiEventStoreController
 * Related Service   : BiEventStoreService, BiEventStoreServiceImpl
 * Related Repository: BiEventStoreRepository
 * Related Entity    : BiEventStore
 * Related DTO       : N/A
 * Related Mapper    : BiEventStoreMapper
 * Related DB Table  : bi_event_stores
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BiEventStoreService, BiEventStoreServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Bi Module against the 'bi_event_stores' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.bi.repository;

import com.plus33.erp.bi.entity.BiEventStore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BiEventStoreRepository extends JpaRepository<BiEventStore, Long> {
    java.util.List<BiEventStore> findByCompanyIdAndEventTypeOrderByOccurredAtDesc(Long companyId, String eventType);
}
