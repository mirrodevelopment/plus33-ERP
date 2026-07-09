/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.eventmesh
 * File              : CloudEvent.java
 * Purpose           : Spring ApplicationEvent published by Integration Module for async processing
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CloudEventController
 * Related Service   : CloudEventService, CloudEventServiceImpl
 * Related Repository: CloudEventRepository
 * Related Entity    : CloudEvent
 * Related DTO       : N/A
 * Related Mapper    : CloudEventMapper
 * Related DB Table  : cloud_events
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Integration Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Spring ApplicationEvent for Integration Module. Published on state changes. Consumed by @EventListener methods for async processing.
 ******************************************************************************/
package com.plus33.erp.integration.eventmesh;

import java.time.LocalDateTime;

public class CloudEvent {
    private String id;
    private String source;
    private String type;
    private String specversion = "1.0";
    private String subject;
    private LocalDateTime time = LocalDateTime.now();
    private String datacontenttype = "application/json";
    private String dataschema;
    private String data;
    private String traceparent;
    private String correlationId;
    private String tenantId;
    private Long sequenceNumber;

    // Default constructor
    public CloudEvent() {}

    public CloudEvent(String id, String source, String type, String data) {
        this.id = id;
        this.source = source;
        this.type = type;
        this.data = data;
    }

    // Getters and setters
    /**
     * Retrieves id data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getId() { return id; }
    /**
     * Performs the setId operation in this module.
     *
     * @param id the unique database ID of the resource
     */
    public void setId(String id) { this.id = id; }
    /**
     * Retrieves source data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSource() { return source; }
    /**
     * Performs the setSource operation in this module.
     *
     * @param source the source entity or DTO to convert
     */
    public void setSource(String source) { this.source = source; }
    /**
     * Retrieves type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getType() { return type; }
    /**
     * Performs the setType operation in this module.
     *
     * @param type the type input value
     */
    public void setType(String type) { this.type = type; }
    /**
     * Retrieves specversion data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSpecversion() { return specversion; }
    /**
     * Performs the setSpecversion operation in this module.
     *
     * @param specversion the specversion input value
     */
    public void setSpecversion(String specversion) { this.specversion = specversion; }
    /**
     * Retrieves subject data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSubject() { return subject; }
    /**
     * Performs the setSubject operation in this module.
     *
     * @param subject the subject input value
     */
    public void setSubject(String subject) { this.subject = subject; }
    /**
     * Retrieves time data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getTime() { return time; }
    /**
     * Performs the setTime operation in this module.
     *
     * @param time the time input value
     */
    public void setTime(LocalDateTime time) { this.time = time; }
    /**
     * Retrieves datacontenttype data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDatacontenttype() { return datacontenttype; }
    /**
     * Performs the setDatacontenttype operation in this module.
     *
     * @param datacontenttype the datacontenttype input value
     */
    public void setDatacontenttype(String datacontenttype) { this.datacontenttype = datacontenttype; }
    /**
     * Retrieves dataschema data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDataschema() { return dataschema; }
    /**
     * Performs the setDataschema operation in this module.
     *
     * @param dataschema the dataschema input value
     */
    public void setDataschema(String dataschema) { this.dataschema = dataschema; }
    /**
     * Retrieves data data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getData() { return data; }
    /**
     * Performs the setData operation in this module.
     *
     * @param data the data input value
     */
    public void setData(String data) { this.data = data; }
    /**
     * Retrieves traceparent data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTraceparent() { return traceparent; }
    /**
     * Performs the setTraceparent operation in this module.
     *
     * @param traceparent the traceparent input value
     */
    public void setTraceparent(String traceparent) { this.traceparent = traceparent; }
    /**
     * Retrieves correlation id data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCorrelationId() { return correlationId; }
    /**
     * Performs the setCorrelationId operation in this module.
     *
     * @param correlationId the correlationId input value
     */
    public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }
    /**
     * Retrieves tenant id data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTenantId() { return tenantId; }
    /**
     * Performs the setTenantId operation in this module.
     *
     * @param tenantId the tenantId input value
     */
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    /**
     * Retrieves sequence number data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getSequenceNumber() { return sequenceNumber; }
    /**
     * Performs the setSequenceNumber operation in this module.
     *
     * @param sequenceNumber the sequenceNumber input value
     */
    public void setSequenceNumber(Long sequenceNumber) { this.sequenceNumber = sequenceNumber; }
}
