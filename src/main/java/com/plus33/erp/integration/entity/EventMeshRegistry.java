/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.entity
 * File              : EventMeshRegistry.java
 * Purpose           : JPA Entity representing a persistent database record in Integration Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EventMeshRegistryController
 * Related Service   : EventMeshRegistryService, EventMeshRegistryServiceImpl
 * Related Repository: EventMeshRegistryRepository
 * Related Entity    : EventMeshRegistry
 * Related DTO       : N/A
 * Related Mapper    : EventMeshRegistryMapper
 * Related DB Table  : bi_event_mesh_registry
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : EventMeshRegistryRepository, EventMeshRegistryMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'bi_event_mesh_registry'. Defines persistent domain object for Integration Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.integration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Integration Module</b>
 *
 * <p><b>Class  :</b> {@code EventMeshRegistry}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.integration.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'bi_event_mesh_registry'.</p>
 *
 * <p><b>Database Table   :</b> {@code bi_event_mesh_registry}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "bi_event_mesh_registry")
public class EventMeshRegistry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotNull
    @Size(max = 250)
    private String topic;

    @Column(name = "publisher_module", nullable = false)
    @NotNull
    @Size(max = 100)
    private String publisherModule;

    @Column(name = "created_at", nullable = false, updatable = false)
    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

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
     * Retrieves publisher module data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPublisherModule() { return publisherModule; }
    /**
     * Performs the setPublisherModule operation in this module.
     *
     * @param publisherModule the publisherModule input value
     */
    public void setPublisherModule(String publisherModule) { this.publisherModule = publisherModule; }
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
}