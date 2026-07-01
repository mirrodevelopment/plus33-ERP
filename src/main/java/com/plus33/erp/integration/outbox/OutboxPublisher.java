package com.plus33.erp.integration.outbox;

import com.plus33.erp.integration.entity.IntegrationOutbox;
import com.plus33.erp.integration.repository.IntegrationOutboxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class OutboxPublisher {
    @Autowired IntegrationOutboxRepository outboxRepo;

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