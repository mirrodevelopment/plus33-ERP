/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.outbox
 * File              : OutboxPublisher.java
 * Purpose           : Business logic service layer for Integration Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: OutboxPublisherController
 * Related Service   : OutboxPublisher
 * Related Repository: OutboxPublisherRepository
 * Related Entity    : OutboxPublisher
 * Related DTO       : N/A
 * Related Mapper    : OutboxPublisherMapper
 * Related DB Table  : outbox_publishers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : OutboxPublisherController, OutboxPublisherImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Integration Module. Implements OutboxPublisherService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.integration.outbox;

import com.plus33.erp.integration.entity.IntegrationOutbox;
import com.plus33.erp.integration.repository.IntegrationOutboxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * <b>PLUS33 Coffee ERP -- Integration Module</b>
 *
 * <p><b>Class  :</b> {@code OutboxPublisher}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.integration.outbox}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Integration Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * OutboxPublisherController
 *   --> OutboxPublisher (this)
 *   --> Validate business rules
 *   --> OutboxPublisherRepository (read/write 'outbox_publishers')
 *   --> OutboxPublisherMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code outbox_publishers}</p>
 * <p><b>Module Deps      :</b> Integration</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class OutboxPublisher {
    @Autowired IntegrationOutboxRepository outboxRepo;
    /**
     * Publishes a domain event to notify dependent modules of the state change.
     *
     * @param topic the topic input value
     * @param eventType the eventType input value
     * @param payload the payload input value
     * @param traceparent the traceparent input value
     * @param correlationId the correlationId input value
     * @param tenantId the tenantId input value
     */
    @Transactional
    public void publishEvent(String topic, String eventType, String payload, String traceparent, String correlationId, String tenantId) {
        IntegrationOutbox outbox = new IntegrationOutbox();
        outbox.setEventId(UUID.randomUUID().toString());
        outbox.setEventType(eventType);
        outbox.setTopic(topic);
        outbox.setPayload(payload);
        outbox.setTraceParent(traceparent);
        outbox.setCorrelationId(correlationId);
        outbox.setTenantId(tenantId);
        outbox.setStatus("PENDING");
        outbox.setAttempts(0);
        outbox.setCreatedAt(LocalDateTime.now());
        outbox.setUpdatedAt(LocalDateTime.now());
        outboxRepo.save(outbox);
    }
}