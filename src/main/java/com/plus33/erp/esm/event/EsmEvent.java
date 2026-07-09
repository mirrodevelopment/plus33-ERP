/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Esm Module
 * Package           : com.plus33.erp.esm.event
 * File              : EsmEvent.java
 * Purpose           : Spring ApplicationEvent published by Esm Module for async processing
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EsmEventController
 * Related Service   : EsmEventService, EsmEventServiceImpl
 * Related Repository: EsmEventRepository
 * Related Entity    : EsmEvent
 * Related DTO       : N/A
 * Related Mapper    : EsmEventMapper
 * Related DB Table  : esm_events
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Esm Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Spring ApplicationEvent for Esm Module. Published on state changes. Consumed by @EventListener methods for async processing.
 ******************************************************************************/
package com.plus33.erp.esm.event;

public class EsmEvent {
    private String eventType;
    private Long companyId;
    private Long referenceId;
    private String details;

    public EsmEvent(String eventType, Long companyId, Long referenceId, String details) {
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
