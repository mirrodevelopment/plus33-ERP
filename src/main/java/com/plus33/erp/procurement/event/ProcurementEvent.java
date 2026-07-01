package com.plus33.erp.procurement.event;

public class ProcurementEvent {
    private String eventType;
    private Long companyId;
    private Long referenceId;
    private String details;

    public ProcurementEvent(String eventType, Long companyId, Long referenceId, String details) {
        this.eventType = eventType;
        this.companyId = companyId;
        this.referenceId = referenceId;
        this.details = details;
    }

    public String getEventType() { return eventType; }
    public Long getCompanyId() { return companyId; }
    public Long getReferenceId() { return referenceId; }
    public String getDetails() { return details; }
}
