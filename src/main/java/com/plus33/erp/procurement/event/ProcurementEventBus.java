/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.event
 * File              : ProcurementEventBus.java
 * Purpose           : Business logic service layer for Procurement Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProcurementEventBusController
 * Related Service   : ProcurementEventBus
 * Related Repository: ProcurementEventStoreItemRepository
 * Related Entity    : ProcurementEventBus
 * Related DTO       : N/A
 * Related Mapper    : ProcurementEventBusMapper
 * Related DB Table  : procurement_event_buss
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProcurementEventBusController, ProcurementEventBusImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Procurement Module. Implements ProcurementEventBusService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.procurement.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plus33.erp.procurement.entity.ProcurementEventStoreItem;
import com.plus33.erp.procurement.repository.ProcurementEventStoreItemRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code ProcurementEventBus}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.event}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Procurement Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * ProcurementEventBusController
 *   --> ProcurementEventBus (this)
 *   --> Validate business rules
 *   --> ProcurementEventBusRepository (read/write 'procurement_event_buss')
 *   --> ProcurementEventBusMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code procurement_event_buss}</p>
 * <p><b>Module Deps      :</b> Procurement</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class ProcurementEventBus {

    private final ProcurementEventStoreItemRepository eventStoreRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ProcurementEventBus(ProcurementEventStoreItemRepository eventStoreRepository, ApplicationEventPublisher eventPublisher) {
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
            ProcurementEventStoreItem item = new ProcurementEventStoreItem();
            item.setCompanyId(companyId);
            item.setEventType(eventType);
            item.setPayloadJson(payloadJson);
            item.setEventVersion("1.0");
            item.setSchemaVersion("1.0");
            item.setCorrelationId(UUID.randomUUID().toString());
            item.setIdempotencyKey(UUID.randomUUID().toString());
            eventStoreRepository.save(item);

            eventPublisher.publishEvent(new ProcurementEvent(eventType, companyId, referenceId, payloadJson));
        } catch (Exception e) {
            throw new RuntimeException("Failed to publish to Procurement Event Bus", e);
        }
    }
}