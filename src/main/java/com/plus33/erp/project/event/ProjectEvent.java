/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Project Module
 * Package           : com.plus33.erp.project.event
 * File              : ProjectEvent.java
 * Purpose           : Spring ApplicationEvent published by Project Module for async processing
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProjectEventController
 * Related Service   : ProjectEventService, ProjectEventServiceImpl
 * Related Repository: ProjectEventRepository
 * Related Entity    : ProjectEvent
 * Related DTO       : N/A
 * Related Mapper    : ProjectEventMapper
 * Related DB Table  : project_events
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Project Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Spring ApplicationEvent for Project Module. Published on state changes. Consumed by @EventListener methods for async processing.
 ******************************************************************************/
package com.plus33.erp.project.event;

public class ProjectEvent {
    private String eventType;
    private Long companyId;
    private Long referenceId;
    private String details;

    public ProjectEvent(String eventType, Long companyId, Long referenceId, String details) {
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
