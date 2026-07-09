/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.outbox
 * File              : OutboxDispatcher.java
 * Purpose           : Business logic service layer for Integration Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: OutboxDispatcherController
 * Related Service   : OutboxDispatcher
 * Related Repository: OutboxDispatcherRepository
 * Related Entity    : OutboxDispatcher
 * Related DTO       : N/A
 * Related Mapper    : OutboxDispatcherMapper
 * Related DB Table  : outbox_dispatchers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : OutboxDispatcherController, OutboxDispatcherImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Integration Module. Implements OutboxDispatcherService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.integration.outbox;

import com.plus33.erp.integration.entity.IntegrationOutbox;
import com.plus33.erp.integration.eventmesh.BrokerRegistry;
import com.plus33.erp.integration.eventmesh.CloudEvent;
import com.plus33.erp.integration.eventmesh.MessageBroker;
import com.plus33.erp.integration.repository.IntegrationOutboxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Integration Module</b>
 *
 * <p><b>Class  :</b> {@code OutboxDispatcher}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.integration.outbox}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Integration Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * OutboxDispatcherController
 *   --> OutboxDispatcher (this)
 *   --> Validate business rules
 *   --> OutboxDispatcherRepository (read/write 'outbox_dispatchers')
 *   --> OutboxDispatcherMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code outbox_dispatchers}</p>
 * <p><b>Module Deps      :</b> Integration</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class OutboxDispatcher {
    @Autowired IntegrationOutboxRepository outboxRepo;
    @Autowired BrokerRegistry brokerRegistry;
    /**
     * Publishes a domain event to notify dependent modules of the state change.
     *
     */
    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void dispatchPendingEvents() {
        List<IntegrationOutbox> pending = outboxRepo.findAll().stream()
                .filter(o -> "PENDING".equals(o.getStatus()))
                .toList();

        MessageBroker broker = brokerRegistry.getActiveBroker();

        for (IntegrationOutbox o : pending) {
            try {
                CloudEvent ce = new CloudEvent(o.getEventId(), "PLUS33-ERP", o.getEventType(), o.getPayload());
                ce.setTraceparent(o.getTraceParent());
                ce.setCorrelationId(o.getCorrelationId());
                ce.setTenantId(o.getTenantId());
                ce.setSequenceNumber(o.getId());

                broker.publish(o.getTopic(), ce);

                o.setStatus("DISPATCHED");
                o.setAttempts(o.getAttempts() + 1);
                o.setUpdatedAt(LocalDateTime.now());
            } catch (Exception e) {
                o.setAttempts(o.getAttempts() + 1);
                if (o.getAttempts() >= 5) {
                    o.setStatus("FAILED");
                }
                o.setUpdatedAt(LocalDateTime.now());
            }
        }
    }
}