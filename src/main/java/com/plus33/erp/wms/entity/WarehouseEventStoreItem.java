/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.entity
 * File              : WarehouseEventStoreItem.java
 * Purpose           : JPA Entity representing a persistent database record in Wms Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WarehouseEventStoreItemController
 * Related Service   : WarehouseEventStoreItemService, WarehouseEventStoreItemServiceImpl
 * Related Repository: WarehouseEventStoreItemRepository
 * Related Entity    : WarehouseEventStoreItem
 * Related DTO       : N/A
 * Related Mapper    : WarehouseEventStoreItemMapper
 * Related DB Table  : warehouse_event_store
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : WarehouseEventStoreItemRepository, WarehouseEventStoreItemMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'warehouse_event_store'. Defines persistent domain object for Wms Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.wms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code WarehouseEventStoreItem}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'warehouse_event_store'.</p>
 *
 * <p><b>Database Table   :</b> {@code warehouse_event_store}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "warehouse_event_store")
public class WarehouseEventStoreItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "aggregate_type", nullable = false, length = 50)
    private String aggregateType;

    @Column(name = "aggregate_id", nullable = false)
    private Long aggregateId;

    @Column(name = "event_type", nullable = false, length = 50)
    private String eventType;

    @Column(name = "payload_json", nullable = false, columnDefinition = "TEXT")
    private String payloadJson;

    @Column(name = "version_number", nullable = false)
    private Long versionNumber = 0L;

    @Column(name = "occurred_at", nullable = false, updatable = false)
    private LocalDateTime occurredAt = LocalDateTime.now();

    @Column(nullable = false)
    private boolean processed = false;

    // Getters and setters
    /**
     * Retrieves id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getId() { return id; }
    /**
     * Performs the setId operation in this module.
     *
     * @param id the unique database ID of the resource
     */
    public void setId(Long id) { this.id = id; }
    /**
     * Retrieves aggregate type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getAggregateType() { return aggregateType; }
    /**
     * Performs the setAggregateType operation in this module.
     *
     * @param aggregateType the aggregateType input value
     */
    public void setAggregateType(String aggregateType) { this.aggregateType = aggregateType; }
    /**
     * Retrieves aggregate id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getAggregateId() { return aggregateId; }
    /**
     * Performs the setAggregateId operation in this module.
     *
     * @param aggregateId the aggregateId input value
     */
    public void setAggregateId(Long aggregateId) { this.aggregateId = aggregateId; }
    /**
     * Retrieves event type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getEventType() { return eventType; }
    /**
     * Performs the setEventType operation in this module.
     *
     * @param eventType the eventType input value
     */
    public void setEventType(String eventType) { this.eventType = eventType; }
    /**
     * Retrieves payload json data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPayloadJson() { return payloadJson; }
    /**
     * Performs the setPayloadJson operation in this module.
     *
     * @param payloadJson the payloadJson input value
     */
    public void setPayloadJson(String payloadJson) { this.payloadJson = payloadJson; }
    /**
     * Retrieves version number data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getVersionNumber() { return versionNumber; }
    /**
     * Performs the setVersionNumber operation in this module.
     *
     * @param versionNumber the versionNumber input value
     */
    public void setVersionNumber(Long versionNumber) { this.versionNumber = versionNumber; }
    /**
     * Retrieves occurred at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getOccurredAt() { return occurredAt; }
    /**
     * Performs the setOccurredAt operation in this module.
     *
     * @param occurredAt the occurredAt input value
     */
    public void setOccurredAt(LocalDateTime occurredAt) { this.occurredAt = occurredAt; }
    /**
     * Performs the isProcessed operation in this module.
     *
     * @return true if operation succeeded, false otherwise
     */
    public boolean isProcessed() { return processed; }
    /**
     * Performs the setProcessed operation in this module.
     *
     * @param processed the processed input value
     */
    public void setProcessed(boolean processed) { this.processed = processed; }
}