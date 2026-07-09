/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.entity
 * File              : IntegrationConnector.java
 * Purpose           : JPA Entity representing a persistent database record in Integration Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: IntegrationConnectorController
 * Related Service   : IntegrationConnectorService, IntegrationConnectorServiceImpl
 * Related Repository: IntegrationConnectorRepository
 * Related Entity    : IntegrationConnector
 * Related DTO       : N/A
 * Related Mapper    : IntegrationConnectorMapper
 * Related DB Table  : integration_connector
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : IntegrationConnectorRepository, IntegrationConnectorMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'integration_connector'. Defines persistent domain object for Integration Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.integration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Integration Module</b>
 *
 * <p><b>Class  :</b> {@code IntegrationConnector}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.integration.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'integration_connector'.</p>
 *
 * <p><b>Database Table   :</b> {@code integration_connector}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "integration_connector")
public class IntegrationConnector {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "connector_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String connectorCode;

    @Column(name = "connector_type", nullable = false)
    @NotNull
    @Size(max = 50)
    private String connectorType;

    @Size(max = 250)
    private String host;

    private Integer port;

    @Column(name = "credential_reference")
    @Size(max = 250)
    private String credentialReference;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "ACTIVE";

    @Column(name = "health_status", nullable = false)
    @NotNull
    @Size(max = 50)
    private String healthStatus = "HEALTHY";

    @Column(name = "last_heartbeat")
    private LocalDateTime lastHeartbeat;

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
     * Retrieves connector code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getConnectorCode() { return connectorCode; }
    /**
     * Performs the setConnectorCode operation in this module.
     *
     * @param connectorCode the connectorCode input value
     */
    public void setConnectorCode(String connectorCode) { this.connectorCode = connectorCode; }
    /**
     * Retrieves connector type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getConnectorType() { return connectorType; }
    /**
     * Performs the setConnectorType operation in this module.
     *
     * @param connectorType the connectorType input value
     */
    public void setConnectorType(String connectorType) { this.connectorType = connectorType; }
    /**
     * Retrieves host data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getHost() { return host; }
    /**
     * Performs the setHost operation in this module.
     *
     * @param host the host input value
     */
    public void setHost(String host) { this.host = host; }
    /**
     * Retrieves port data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getPort() { return port; }
    /**
     * Performs the setPort operation in this module.
     *
     * @param port the port input value
     */
    public void setPort(Integer port) { this.port = port; }
    /**
     * Retrieves credential reference data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCredentialReference() { return credentialReference; }
    /**
     * Performs the setCredentialReference operation in this module.
     *
     * @param credentialReference the credentialReference input value
     */
    public void setCredentialReference(String credentialReference) { this.credentialReference = credentialReference; }
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
     * Retrieves health status data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getHealthStatus() { return healthStatus; }
    /**
     * Performs the setHealthStatus operation in this module.
     *
     * @param healthStatus the healthStatus input value
     */
    public void setHealthStatus(String healthStatus) { this.healthStatus = healthStatus; }
    /**
     * Retrieves last heartbeat data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getLastHeartbeat() { return lastHeartbeat; }
    /**
     * Performs the setLastHeartbeat operation in this module.
     *
     * @param lastHeartbeat the lastHeartbeat input value
     */
    public void setLastHeartbeat(LocalDateTime lastHeartbeat) { this.lastHeartbeat = lastHeartbeat; }
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