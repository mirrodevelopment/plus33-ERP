/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Project Module
 * Package           : com.plus33.erp.project.event
 * File              : ProjectEventBus.java
 * Purpose           : Business logic service layer for Project Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProjectEventBusController
 * Related Service   : ProjectEventBus
 * Related Repository: ProjectEventStoreItemRepository
 * Related Entity    : ProjectEventBus
 * Related DTO       : N/A
 * Related Mapper    : ProjectEventBusMapper
 * Related DB Table  : project_event_buss
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProjectEventBusController, ProjectEventBusImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Project Module. Implements ProjectEventBusService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.project.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plus33.erp.project.entity.ProjectEventStoreItem;
import com.plus33.erp.project.repository.ProjectEventStoreItemRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * <b>PLUS33 Coffee ERP -- Project Module</b>
 *
 * <p><b>Class  :</b> {@code ProjectEventBus}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.project.event}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Project Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * ProjectEventBusController
 *   --> ProjectEventBus (this)
 *   --> Validate business rules
 *   --> ProjectEventBusRepository (read/write 'project_event_buss')
 *   --> ProjectEventBusMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code project_event_buss}</p>
 * <p><b>Module Deps      :</b> Project</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class ProjectEventBus {

    private final ProjectEventStoreItemRepository eventStoreRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ProjectEventBus(ProjectEventStoreItemRepository eventStoreRepository, ApplicationEventPublisher eventPublisher) {
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
            ProjectEventStoreItem item = new ProjectEventStoreItem();
            item.setCompanyId(companyId);
            item.setEventType(eventType);
            item.setPayloadJson(payloadJson);
            item.setEventVersion("1.0");
            item.setSchemaVersion("1.0");
            item.setCorrelationId(UUID.randomUUID().toString());
            item.setIdempotencyKey(UUID.randomUUID().toString());
            eventStoreRepository.save(item);

            eventPublisher.publishEvent(new ProjectEvent(eventType, companyId, referenceId, payloadJson));
        } catch (Exception e) {
            throw new RuntimeException("Failed to publish to Project Event Bus", e);
        }
    }
}