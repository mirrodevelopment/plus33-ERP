/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.entity
 * File              : BiBrokerRegistry.java
 * Purpose           : JPA Entity representing a persistent database record in Integration Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiBrokerRegistryController
 * Related Service   : BiBrokerRegistryService, BiBrokerRegistryServiceImpl
 * Related Repository: BiBrokerRegistryRepository
 * Related Entity    : BiBrokerRegistry
 * Related DTO       : N/A
 * Related Mapper    : BiBrokerRegistryMapper
 * Related DB Table  : bi_broker_registry
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BiBrokerRegistryRepository, BiBrokerRegistryMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'bi_broker_registry'. Defines persistent domain object for Integration Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.integration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Integration Module</b>
 *
 * <p><b>Class  :</b> {@code BiBrokerRegistry}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.integration.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'bi_broker_registry'.</p>
 *
 * <p><b>Database Table   :</b> {@code bi_broker_registry}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "bi_broker_registry")
public class BiBrokerRegistry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "broker_name", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String brokerName;

    @Column(name = "broker_type", nullable = false)
    @NotNull
    @Size(max = 50)
    private String brokerType;

    @Column(name = "connection_url", nullable = false)
    @NotNull
    @Size(max = 250)
    private String connectionUrl;

    @Column(nullable = false)
    @NotNull
    private Boolean active = true;

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
     * Retrieves broker name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getBrokerName() { return brokerName; }
    /**
     * Performs the setBrokerName operation in this module.
     *
     * @param brokerName the brokerName input value
     */
    public void setBrokerName(String brokerName) { this.brokerName = brokerName; }
    /**
     * Retrieves broker type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getBrokerType() { return brokerType; }
    /**
     * Performs the setBrokerType operation in this module.
     *
     * @param brokerType the brokerType input value
     */
    public void setBrokerType(String brokerType) { this.brokerType = brokerType; }
    /**
     * Retrieves connection url data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getConnectionUrl() { return connectionUrl; }
    /**
     * Performs the setConnectionUrl operation in this module.
     *
     * @param connectionUrl the connectionUrl input value
     */
    public void setConnectionUrl(String connectionUrl) { this.connectionUrl = connectionUrl; }
    /**
     * Retrieves active data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getActive() { return active; }
    /**
     * Performs the setActive operation in this module.
     *
     * @param active the active input value
     */
    public void setActive(Boolean active) { this.active = active; }
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