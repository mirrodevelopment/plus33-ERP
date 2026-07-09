/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.event
 * File              : GrcEventBus.java
 * Purpose           : Component of Grc Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: GrcEventBusController
 * Related Service   : GrcEventBusService, GrcEventBusServiceImpl
 * Related Repository: GrcEventStoreRepository
 * Related Entity    : GrcEventBus
 * Related DTO       : N/A
 * Related Mapper    : GrcEventBusMapper
 * Related DB Table  : grc_event_buss
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Grc Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Grc Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.grc.event;

import com.plus33.erp.grc.entity.GrcEventStoreItem;
import com.plus33.erp.grc.repository.GrcEventStoreRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

/**
 * <b>PLUS33 Coffee ERP -- Grc Module</b>
 *
 * <p><b>Class  :</b> {@code GrcEventBus}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.grc.event}</p>
 * <p><b>Layer  :</b> Spring Component: shared utility or infrastructure helper for Grc Module.</p>
 *
 * <p><b>Module Deps      :</b> Grc</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Component
public class GrcEventBus {

    private final GrcEventStoreRepository eventStore;
    private final ApplicationEventPublisher publisher;

    public GrcEventBus(GrcEventStoreRepository eventStore, ApplicationEventPublisher publisher) {
        this.eventStore = eventStore;
        this.publisher = publisher;
    }

    /**
     * Publishes a domain event to notify dependent modules of the state change.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param eventType the eventType input value
     * @param payload the payload input value
     */
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