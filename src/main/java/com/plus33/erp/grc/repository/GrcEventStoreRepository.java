/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.repository
 * File              : GrcEventStoreRepository.java
 * Purpose           : JPA Repository providing database CRUD for Grc Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: GrcEventStoreController
 * Related Service   : GrcEventStoreService, GrcEventStoreServiceImpl
 * Related Repository: GrcEventStoreRepository
 * Related Entity    : GrcEventStore
 * Related DTO       : N/A
 * Related Mapper    : GrcEventStoreMapper
 * Related DB Table  : grc_event_stores
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : GrcEventStoreService, GrcEventStoreServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Grc Module against the 'grc_event_stores' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.grc.repository;
import com.plus33.erp.grc.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface GrcEventStoreRepository extends JpaRepository<GrcEventStoreItem, Long> {
    Optional<GrcEventStoreItem> findByIdempotencyKey(String key);
    List<GrcEventStoreItem> findByCompanyIdAndEventType(Long companyId, String eventType);
    long countByEventType(String eventType);
}
