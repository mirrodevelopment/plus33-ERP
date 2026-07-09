/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.entity
 * File              : IntegrationInbox.java
 * Purpose           : JPA Entity representing a persistent database record in Integration Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: IntegrationInboxController
 * Related Service   : IntegrationInboxService, IntegrationInboxServiceImpl
 * Related Repository: IntegrationInboxRepository
 * Related Entity    : IntegrationInbox
 * Related DTO       : N/A
 * Related Mapper    : IntegrationInboxMapper
 * Related DB Table  : integration_inbox
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : IntegrationInboxRepository, IntegrationInboxMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'integration_inbox'. Defines persistent domain object for Integration Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.integration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Integration Module</b>
 *
 * <p><b>Class  :</b> {@code IntegrationInbox}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.integration.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'integration_inbox'.</p>
 *
 * <p><b>Database Table   :</b> {@code integration_inbox}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "integration_inbox")
public class IntegrationInbox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String eventId;

    @Column(nullable = false)
    @NotNull
    @Size(max = 250)
    private String topic;

    @Column(name = "received_at", nullable = false, updatable = false)
    @NotNull
    private LocalDateTime receivedAt = LocalDateTime.now();

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
     * Retrieves received at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getReceivedAt() { return receivedAt; }
    /**
     * Performs the setReceivedAt operation in this module.
     *
     * @param receivedAt the receivedAt input value
     */
    public void setReceivedAt(LocalDateTime receivedAt) { this.receivedAt = receivedAt; }
}