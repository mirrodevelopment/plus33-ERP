package com.plus33.erp.project.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plus33.erp.project.entity.ProjectEventStoreItem;
import com.plus33.erp.project.repository.ProjectEventStoreItemRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ProjectEventBus {

    private final ProjectEventStoreItemRepository eventStoreRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ProjectEventBus(ProjectEventStoreItemRepository eventStoreRepository, ApplicationEventPublisher eventPublisher) {
        this.eventStoreRepository = eventStoreRepository;
        this.eventPublisher = eventPublisher;
    }

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
