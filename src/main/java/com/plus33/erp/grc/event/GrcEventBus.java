package com.plus33.erp.grc.event;

import com.plus33.erp.grc.entity.GrcEventStoreItem;
import com.plus33.erp.grc.repository.GrcEventStoreRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class GrcEventBus {

    private final GrcEventStoreRepository eventStore;
    private final ApplicationEventPublisher publisher;

    public GrcEventBus(GrcEventStoreRepository eventStore, ApplicationEventPublisher publisher) {
        this.eventStore = eventStore;
        this.publisher = publisher;
    }

    public void publish(Long companyId, String eventType, Map<String, Object> payload) {
        String idempotencyKey = eventType + "-" + UUID.randomUUID();
        GrcEventStoreItem item = new GrcEventStoreItem();
        item.setCompanyId(companyId != null ? companyId : 0L);
        item.setEventType(eventType);
        item.setPayloadJson(payload.toString());
        item.setIdempotencyKey(idempotencyKey);
        eventStore.save(item);
        publisher.publishEvent(new GrcEvent(this, eventType, companyId, payload));
    }
}
