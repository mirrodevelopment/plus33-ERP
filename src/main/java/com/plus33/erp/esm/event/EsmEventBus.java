/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Esm Module
 * Package           : com.plus33.erp.esm.event
 * File              : EsmEventBus.java
 * Purpose           : Business logic service layer for Esm Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EsmEventBusController
 * Related Service   : EsmEventBus
 * Related Repository: EsmEventStoreItemRepository
 * Related Entity    : EsmEventBus
 * Related DTO       : N/A
 * Related Mapper    : EsmEventBusMapper
 * Related DB Table  : esm_event_buss
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : EsmEventBusController, EsmEventBusImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Esm Module. Implements EsmEventBusService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.esm.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plus33.erp.esm.entity.EsmEventStoreItem;
import com.plus33.erp.esm.repository.EsmEventStoreItemRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * <b>PLUS33 Coffee ERP -- Esm Module</b>
 *
 * <p><b>Class  :</b> {@code EsmEventBus}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.esm.event}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Esm Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * EsmEventBusController
 *   --> EsmEventBus (this)
 *   --> Validate business rules
 *   --> EsmEventBusRepository (read/write 'esm_event_buss')
 *   --> EsmEventBusMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code esm_event_buss}</p>
 * <p><b>Module Deps      :</b> Esm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class EsmEventBus {

    private final EsmEventStoreItemRepository eventStoreRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public EsmEventBus(EsmEventStoreItemRepository eventStoreRepository, ApplicationEventPublisher eventPublisher) {
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
            EsmEventStoreItem item = new EsmEventStoreItem();
            item.setCompanyId(companyId);
            item.setEventType(eventType);
            item.setPayloadJson(payloadJson);
            item.setEventVersion("1.0");
            item.setSchemaVersion("1.0");
            item.setCorrelationId(UUID.randomUUID().toString());
            item.setIdempotencyKey(UUID.randomUUID().toString());
            eventStoreRepository.save(item);

            eventPublisher.publishEvent(new EsmEvent(eventType, companyId, referenceId, payloadJson));
        } catch (Exception e) {
            throw new RuntimeException("Failed to publish event to ESM Event Bus", e);
        }
    }
}