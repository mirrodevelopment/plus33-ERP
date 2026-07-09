/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.repository
 * File              : OfflineQueueItemRepository.java
 * Purpose           : JPA Repository providing database CRUD for Wms Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: OfflineQueueItemController
 * Related Service   : OfflineQueueItemService, OfflineQueueItemServiceImpl
 * Related Repository: OfflineQueueItemRepository
 * Related Entity    : OfflineQueueItem
 * Related DTO       : N/A
 * Related Mapper    : OfflineQueueItemMapper
 * Related DB Table  : offline_queue_items
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : OfflineQueueItemService, OfflineQueueItemServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Wms Module against the 'offline_queue_items' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.OfflineQueueItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code OfflineQueueItemRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'offline_queue_items' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code offline_queue_items}</p>
 * <p><b>Module Deps      :</b> Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface OfflineQueueItemRepository extends JpaRepository<OfflineQueueItem, Long> {
    List<OfflineQueueItem> findByCompanyIdAndDeviceIdAndStatus(Long companyId, String deviceId, String status);
    List<OfflineQueueItem> findByStatusOrderByLoggedAtAsc(String status);
}
