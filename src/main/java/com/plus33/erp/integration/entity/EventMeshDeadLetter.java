/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.entity
 * File              : EventMeshDeadLetter.java
 * Purpose           : JPA Entity representing a persistent database record in Integration Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EventMeshDeadLetterController
 * Related Service   : EventMeshDeadLetterService, EventMeshDeadLetterServiceImpl
 * Related Repository: EventMeshDeadLetterRepository
 * Related Entity    : EventMeshDeadLetter
 * Related DTO       : N/A
 * Related Mapper    : EventMeshDeadLetterMapper
 * Related DB Table  : bi_event_mesh_dead_letter
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : EventMeshDeadLetterRepository, EventMeshDeadLetterMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'bi_event_mesh_dead_letter'. Defines persistent domain object for Integration Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.integration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Integration Module</b>
 *
 * <p><b>Class  :</b> {@code EventMeshDeadLetter}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.integration.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'bi_event_mesh_dead_letter'.</p>
 *
 * <p><b>Database Table   :</b> {@code bi_event_mesh_dead_letter}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "bi_event_mesh_dead_letter")
public class EventMeshDeadLetter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false)
    @NotNull
    @Size(max = 100)
    private String eventId;

    @Column(nullable = false)
    @NotNull
    @Size(max = 250)
    private String topic;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String payload;

    @Column(name = "exception_message", columnDefinition = "TEXT")
    private String exceptionMessage;

    @Column(name = "retry_count", nullable = false)
    @NotNull
    private Integer retryCount = 0;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "PENDING_REPLAY";

    @Column(name = "created_at", nullable = false, updatable = false)
    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    @NotNull
    private LocalDateTime updatedAt = LocalDateTime.now();

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
     * Retrieves event id data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getEventId() { return eventId; }
    /**
     * Performs the setEventId operation in this module.
     *
     * @param eventId the eventId input value
     */
    public void setEventId(String eventId) { this.eventId = eventId; }
    /**
     * Retrieves topic data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTopic() { return topic; }
    /**
     * Performs the setTopic operation in this module.
     *
     * @param topic the topic input value
     */
    public void setTopic(String topic) { this.topic = topic; }
    /**
     * Retrieves payload data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPayload() { return payload; }
    /**
     * Performs the setPayload operation in this module.
     *
     * @param payload the payload input value
     */
    public void setPayload(String payload) { this.payload = payload; }
    /**
     * Retrieves exception message data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getExceptionMessage() { return exceptionMessage; }
    /**
     * Performs the setExceptionMessage operation in this module.
     *
     * @param exceptionMessage the exceptionMessage input value
     */
    public void setExceptionMessage(String exceptionMessage) { this.exceptionMessage = exceptionMessage; }
    /**
     * Retrieves retry count data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getRetryCount() { return retryCount; }
    /**
     * Performs the setRetryCount operation in this module.
     *
     * @param retryCount the retryCount input value
     */
    public void setRetryCount(Integer retryCount) { this.retryCount = retryCount; }
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
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
    /**
     * Performs the setCreatedAt operation in this module.
     *
     * @param createdAt the createdAt input value
     */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    /**
     * Retrieves updated at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    /**
     * Performs the setUpdatedAt operation in this module.
     *
     * @param updatedAt the updatedAt input value
     */
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}