/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Hcm Module
 * Package           : com.plus33.erp.hcm.event
 * File              : HcmEventBus.java
 * Purpose           : Business logic service layer for Hcm Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: HcmEventBusController
 * Related Service   : HcmEventBus
 * Related Repository: HcmEventStoreItemRepository
 * Related Entity    : HcmEventBus
 * Related DTO       : N/A
 * Related Mapper    : HcmEventBusMapper
 * Related DB Table  : hcm_event_buss
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : HcmEventBusController, HcmEventBusImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Hcm Module. Implements HcmEventBusService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.hcm.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plus33.erp.hcm.entity.HcmEventStoreItem;
import com.plus33.erp.hcm.repository.HcmEventStoreItemRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * <b>PLUS33 Coffee ERP -- Hcm Module</b>
 *
 * <p><b>Class  :</b> {@code HcmEventBus}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.hcm.event}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Hcm Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * HcmEventBusController
 *   --> HcmEventBus (this)
 *   --> Validate business rules
 *   --> HcmEventBusRepository (read/write 'hcm_event_buss')
 *   --> HcmEventBusMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code hcm_event_buss}</p>
 * <p><b>Module Deps      :</b> Hcm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class HcmEventBus {

    private final HcmEventStoreItemRepository eventStoreRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public HcmEventBus(HcmEventStoreItemRepository eventStoreRepository, ApplicationEventPublisher eventPublisher) {
        this.eventStoreRepository = eventStoreRepository;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Publishes a domain event to notify dependent modules of the state change.
     *
     * @param eventType the eventType input value
     * @param companyId owning company ID for multi-tenant data isolation
     * @param referenceId the referenceId input value
     * @param payload the payload input value
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void publish(String eventType, Long companyId, Long referenceId, Object payload) {
        try {
            String payloadJson = objectMapper.writeValueAsString(payload);
            HcmEventStoreItem item = new HcmEventStoreItem();
            item.setCompanyId(companyId);
            item.setEventType(eventType);
            item.setPayloadJson(payloadJson);
            item.setEventVersion("1.0");
            item.setSchemaVersion("1.0");
            item.setCorrelationId(UUID.randomUUID().toString());
            item.setIdempotencyKey(UUID.randomUUID().toString());
            eventStoreRepository.save(item);

            eventPublisher.publishEvent(new HcmEvent(eventType, companyId, referenceId, payloadJson));
        } catch (Exception e) {
            throw new RuntimeException("Failed to publish to HCM Event Bus", e);
        }
    }
}