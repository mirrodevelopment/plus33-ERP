/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.repository
 * File              : PlatformEdgeSyncQueueRepository.java
 * Purpose           : JPA Repository providing database CRUD for Platform Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformEdgeSyncQueueController
 * Related Service   : PlatformEdgeSyncQueueService, PlatformEdgeSyncQueueServiceImpl
 * Related Repository: PlatformEdgeSyncQueueRepository
 * Related Entity    : PlatformEdgeSyncQueue
 * Related DTO       : N/A
 * Related Mapper    : PlatformEdgeSyncQueueMapper
 * Related DB Table  : platform_edge_sync_queues
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformEdgeSyncQueueService, PlatformEdgeSyncQueueServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Platform Module against the 'platform_edge_sync_queues' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformEdgeSyncQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformEdgeSyncQueueRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'platform_edge_sync_queues' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_edge_sync_queues}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface PlatformEdgeSyncQueueRepository extends JpaRepository<PlatformEdgeSyncQueue, Long> {
}