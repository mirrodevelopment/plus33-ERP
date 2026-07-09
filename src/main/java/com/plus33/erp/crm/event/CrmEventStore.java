/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Crm Module
 * Package           : com.plus33.erp.crm.event
 * File              : CrmEventStore.java
 * Purpose           : Business logic service layer for Crm Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CrmEventStoreController
 * Related Service   : CrmEventStore
 * Related Repository: CrmEventStoreRepository
 * Related Entity    : CrmEventStore
 * Related DTO       : N/A
 * Related Mapper    : CrmEventStoreMapper
 * Related DB Table  : crm_event_stores
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CrmEventStoreController, CrmEventStoreImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Crm Module. Implements CrmEventStoreService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.crm.event;

import com.plus33.erp.crm.entity.CrmEventStoreItem;
import com.plus33.erp.crm.repository.CrmEventStoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Crm Module</b>
 *
 * <p><b>Class  :</b> {@code CrmEventStore}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.crm.event}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Crm Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * CrmEventStoreController
 *   --> CrmEventStore (this)
 *   --> Validate business rules
 *   --> CrmEventStoreRepository (read/write 'crm_event_stores')
 *   --> CrmEventStoreMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code crm_event_stores}</p>
 * <p><b>Module Deps      :</b> Crm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@Transactional
public class CrmEventStore {

    private final CrmEventStoreRepository repo;

    public CrmEventStore(CrmEventStoreRepository repo) {
        this.repo = repo;
    }

    /**
     * Performs the recordEvent operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param eventType the eventType input value
     * @param payload the payload input value
     * @param key the key input value
     * @return the CrmEventStoreItem result
     */
    public CrmEventStoreItem recordEvent(Long companyId, String eventType, String payload, String key) {
        CrmEventStoreItem item = new CrmEventStoreItem();
        item.setCompanyId(companyId);
        item.setEventType(eventType);
        item.setPayloadJson(payload);
        item.setIdempotencyKey(key);
        return repo.save(item);
    }

    /**
     * Retrieves events data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Transactional(readOnly = true)
    public List<CrmEventStoreItem> getEvents(Long companyId) {
        return repo.findByCompanyIdOrderByOccurredAtAsc(companyId);
    }
}