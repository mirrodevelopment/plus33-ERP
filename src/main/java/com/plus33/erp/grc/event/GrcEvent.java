/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.event
 * File              : GrcEvent.java
 * Purpose           : Spring ApplicationEvent published by Grc Module for async processing
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: GrcEventController
 * Related Service   : GrcEventService, GrcEventServiceImpl
 * Related Repository: GrcEventRepository
 * Related Entity    : GrcEvent
 * Related DTO       : N/A
 * Related Mapper    : GrcEventMapper
 * Related DB Table  : grc_events
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Grc Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Spring ApplicationEvent for Grc Module. Published on state changes. Consumed by @EventListener methods for async processing.
 ******************************************************************************/
package com.plus33.erp.grc.event;

import org.springframework.context.ApplicationEvent;
import java.util.Map;

public class GrcEvent extends ApplicationEvent {
    private final String eventType;
    private final Long companyId;
    private final Map<String, Object> payload;

    public GrcEvent(Object source, String eventType, Long companyId, Map<String, Object> payload) {
        super(source);
        this.eventType = eventType;
        this.companyId = companyId;
        this.payload = payload;
    }

    /**
     * Retrieves event type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getEventType() { return eventType; }
    /**
     * Retrieves company id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCompanyId() { return companyId; }
    /**
     * Retrieves payload data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Map<String, Object> getPayload() { return payload; }
}
