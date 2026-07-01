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

    public String getEventType() { return eventType; }
    public Long getCompanyId() { return companyId; }
    public Map<String, Object> getPayload() { return payload; }
}
