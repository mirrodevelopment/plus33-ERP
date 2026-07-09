/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.event
 * File              : ProcurementEvent.java
 * Purpose           : Spring ApplicationEvent published by Procurement Module for async processing
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProcurementEventController
 * Related Service   : ProcurementEventService, ProcurementEventServiceImpl
 * Related Repository: ProcurementEventRepository
 * Related Entity    : ProcurementEvent
 * Related DTO       : N/A
 * Related Mapper    : ProcurementEventMapper
 * Related DB Table  : procurement_events
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Procurement Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Spring ApplicationEvent for Procurement Module. Published on state changes. Consumed by @EventListener methods for async processing.
 ******************************************************************************/
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
     * Retrieves reference id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getReferenceId() { return referenceId; }
    /**
     * Retrieves details data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDetails() { return details; }
}
