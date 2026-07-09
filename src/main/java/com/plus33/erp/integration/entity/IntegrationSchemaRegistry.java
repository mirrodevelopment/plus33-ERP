/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.entity
 * File              : IntegrationSchemaRegistry.java
 * Purpose           : JPA Entity representing a persistent database record in Integration Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: IntegrationSchemaRegistryController
 * Related Service   : IntegrationSchemaRegistryService, IntegrationSchemaRegistryServiceImpl
 * Related Repository: IntegrationSchemaRegistryRepository
 * Related Entity    : IntegrationSchemaRegistry
 * Related DTO       : N/A
 * Related Mapper    : IntegrationSchemaRegistryMapper
 * Related DB Table  : integration_schema_registry
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : IntegrationSchemaRegistryRepository, IntegrationSchemaRegistryMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'integration_schema_registry'. Defines persistent domain object for Integration Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.integration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Integration Module</b>
 *
 * <p><b>Class  :</b> {@code IntegrationSchemaRegistry}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.integration.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'integration_schema_registry'.</p>
 *
 * <p><b>Database Table   :</b> {@code integration_schema_registry}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "integration_schema_registry", uniqueConstraints = @UniqueConstraint(columnNames = {"event_type", "schema_version"}))
public class IntegrationSchemaRegistry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_type", nullable = false)
    @NotNull
    @Size(max = 250)
    private String eventType;

    @Column(name = "schema_version", nullable = false)
    @NotNull
    @Size(max = 50)
    private String schemaVersion;

    @Column(name = "schema_definition", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String schemaDefinition;

    @Column(name = "compatibility_rule", nullable = false)
    @NotNull
    @Size(max = 50)
    private String compatibilityRule = "BACKWARD";

    @Column(name = "deprecation_date")
    private LocalDateTime deprecationDate;

    @Column(name = "replacement_event")
    @Size(max = 250)
    private String replacementEvent;

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
     * Retrieves schema version data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSchemaVersion() { return schemaVersion; }
    /**
     * Performs the setSchemaVersion operation in this module.
     *
     * @param schemaVersion the schemaVersion input value
     */
    public void setSchemaVersion(String schemaVersion) { this.schemaVersion = schemaVersion; }
    /**
     * Retrieves schema definition data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSchemaDefinition() { return schemaDefinition; }
    /**
     * Performs the setSchemaDefinition operation in this module.
     *
     * @param schemaDefinition the schemaDefinition input value
     */
    public void setSchemaDefinition(String schemaDefinition) { this.schemaDefinition = schemaDefinition; }
    /**
     * Retrieves compatibility rule data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCompatibilityRule() { return compatibilityRule; }
    /**
     * Performs the setCompatibilityRule operation in this module.
     *
     * @param compatibilityRule the compatibilityRule input value
     */
    public void setCompatibilityRule(String compatibilityRule) { this.compatibilityRule = compatibilityRule; }
    /**
     * Retrieves deprecation date data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getDeprecationDate() { return deprecationDate; }
    /**
     * Performs the setDeprecationDate operation in this module.
     *
     * @param deprecationDate the deprecationDate input value
     */
    public void setDeprecationDate(LocalDateTime deprecationDate) { this.deprecationDate = deprecationDate; }
    /**
     * Retrieves replacement event data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getReplacementEvent() { return replacementEvent; }
    /**
     * Performs the setReplacementEvent operation in this module.
     *
     * @param replacementEvent the replacementEvent input value
     */
    public void setReplacementEvent(String replacementEvent) { this.replacementEvent = replacementEvent; }
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