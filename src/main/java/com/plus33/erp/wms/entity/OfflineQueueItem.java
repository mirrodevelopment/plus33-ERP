/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.entity
 * File              : OfflineQueueItem.java
 * Purpose           : JPA Entity representing a persistent database record in Wms Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: OfflineQueueItemController
 * Related Service   : OfflineQueueItemService, OfflineQueueItemServiceImpl
 * Related Repository: OfflineQueueItemRepository
 * Related Entity    : OfflineQueueItem
 * Related DTO       : N/A
 * Related Mapper    : OfflineQueueItemMapper
 * Related DB Table  : offline_event_queue
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : OfflineQueueItemRepository, OfflineQueueItemMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'offline_event_queue'. Defines persistent domain object for Wms Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.wms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code OfflineQueueItem}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'offline_event_queue'.</p>
 *
 * <p><b>Database Table   :</b> {@code offline_event_queue}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "offline_event_queue")
public class OfflineQueueItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "device_id", nullable = false, length = 100)
    private String deviceId;

    @Column(name = "event_type", nullable = false, length = 50)
    private String eventType;

    @Column(name = "payload_json", nullable = false, columnDefinition = "TEXT")
    private String payloadJson;

    @Column(nullable = false, length = 30)
    private String status = "PENDING";

    @Column(name = "logged_at", nullable = false)
    private LocalDateTime loggedAt;

    @Column(name = "synced_at")
    private LocalDateTime syncedAt;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

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
     * Retrieves company id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCompanyId() { return companyId; }
    /**
     * Performs the setCompanyId operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     */
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    /**
     * Retrieves device id data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDeviceId() { return deviceId; }
    /**
     * Performs the setDeviceId operation in this module.
     *
     * @param deviceId the deviceId input value
     */
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
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
     * Retrieves status data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getStatus() { return status; }
    /**
     * Performs the setStatus operation in this module.
     *
     * @param status status filter for narrowing query results
     */
    public void setStatus(String status) { this.status = status; }
    /**
     * Retrieves logged at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getLoggedAt() { return loggedAt; }
    /**
     * Performs the setLoggedAt operation in this module.
     *
     * @param loggedAt the loggedAt input value
     */
    public void setLoggedAt(LocalDateTime loggedAt) { this.loggedAt = loggedAt; }
    /**
     * Retrieves synced at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getSyncedAt() { return syncedAt; }
    /**
     * Performs the setSyncedAt operation in this module.
     *
     * @param syncedAt the syncedAt input value
     */
    public void setSyncedAt(LocalDateTime syncedAt) { this.syncedAt = syncedAt; }
    /**
     * Retrieves error message data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getErrorMessage() { return errorMessage; }
    /**
     * Performs the setErrorMessage operation in this module.
     *
     * @param errorMessage the errorMessage input value
     */
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}