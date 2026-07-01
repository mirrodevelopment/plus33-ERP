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

@Service
public class OutboxDispatcher {
    @Autowired IntegrationOutboxRepository outboxRepo;
    @Autowired BrokerRegistry brokerRegistry;

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