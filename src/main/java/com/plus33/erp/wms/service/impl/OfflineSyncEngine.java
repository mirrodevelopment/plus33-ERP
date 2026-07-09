/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.service.impl
 * File              : OfflineSyncEngine.java
 * Purpose           : Business logic service layer for Wms Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: OfflineSyncEngineController
 * Related Service   : OfflineSyncEngine
 * Related Repository: OfflineQueueItemRepository
 * Related Entity    : OfflineSyncEngine
 * Related DTO       : N/A
 * Related Mapper    : OfflineSyncEngineMapper
 * Related DB Table  : offline_sync_engines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : OfflineSyncEngineController, OfflineSyncEngineImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Wms Module. Implements OfflineSyncEngineService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.wms.service.impl;

import com.plus33.erp.wms.entity.OfflineQueueItem;
import com.plus33.erp.wms.repository.OfflineQueueItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code OfflineSyncEngine}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.service.impl}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Wms Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * OfflineSyncEngineController
 *   --> OfflineSyncEngine (this)
 *   --> Validate business rules
 *   --> OfflineSyncEngineRepository (read/write 'offline_sync_engines')
 *   --> OfflineSyncEngineMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code offline_sync_engines}</p>
 * <p><b>Module Deps      :</b> Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@Transactional
public class OfflineSyncEngine {

    private final OfflineQueueItemRepository queueRepo;

    public OfflineSyncEngine(OfflineQueueItemRepository queueRepo) {
        this.queueRepo = queueRepo;
    }

    /**
     * Performs the queueOfflineTransaction operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param deviceId the deviceId input value
     * @param eventType the eventType input value
     * @param payloadJson the payloadJson input value
     * @return the OfflineQueueItem result
     */
    public OfflineQueueItem queueOfflineTransaction(Long companyId, String deviceId, String eventType, String payloadJson) {
        OfflineQueueItem item = new OfflineQueueItem();
        item.setCompanyId(companyId);
        item.setDeviceId(deviceId);
        item.setEventType(eventType);
        item.setPayloadJson(payloadJson);
        item.setLoggedAt(LocalDateTime.now());
        item.setStatus("PENDING");
        return queueRepo.save(item);
    }

    /**
     * Performs the replayQueue operation in this module.
     *
     * @return List of matching records
     */
    public List<OfflineQueueItem> replayQueue() {
        List<OfflineQueueItem> items = queueRepo.findByStatusOrderByLoggedAtAsc("PENDING");
        items.forEach(item -> {
            item.setStatus("SYNCED");
            item.setSyncedAt(LocalDateTime.now());
        });
        return queueRepo.saveAll(items);
    }
}