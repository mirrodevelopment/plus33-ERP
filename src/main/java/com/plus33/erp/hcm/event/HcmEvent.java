/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Hcm Module
 * Package           : com.plus33.erp.hcm.event
 * File              : HcmEvent.java
 * Purpose           : Spring ApplicationEvent published by Hcm Module for async processing
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: HcmEventController
 * Related Service   : HcmEventService, HcmEventServiceImpl
 * Related Repository: HcmEventRepository
 * Related Entity    : HcmEvent
 * Related DTO       : N/A
 * Related Mapper    : HcmEventMapper
 * Related DB Table  : hcm_events
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Hcm Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Spring ApplicationEvent for Hcm Module. Published on state changes. Consumed by @EventListener methods for async processing.
 ******************************************************************************/
package com.plus33.erp.hcm.event;

public class HcmEvent {
    private String eventType;
    private Long companyId;
    private Long referenceId;
    private String details;

    public HcmEvent(String eventType, Long companyId, Long referenceId, String details) {
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
