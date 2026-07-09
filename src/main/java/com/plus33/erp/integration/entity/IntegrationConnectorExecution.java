/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.entity
 * File              : IntegrationConnectorExecution.java
 * Purpose           : JPA Entity representing a persistent database record in Integration Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: IntegrationConnectorExecutionController
 * Related Service   : IntegrationConnectorExecutionService, IntegrationConnectorExecutionServiceImpl
 * Related Repository: IntegrationConnectorExecutionRepository
 * Related Entity    : IntegrationConnectorExecution
 * Related DTO       : N/A
 * Related Mapper    : IntegrationConnectorExecutionMapper
 * Related DB Table  : integration_connector_execution
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : IntegrationConnectorExecutionRepository, IntegrationConnectorExecutionMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'integration_connector_execution'. Defines persistent domain object for Integration Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.integration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Integration Module</b>
 *
 * <p><b>Class  :</b> {@code IntegrationConnectorExecution}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.integration.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'integration_connector_execution'.</p>
 *
 * <p><b>Database Table   :</b> {@code integration_connector_execution}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "integration_connector_execution")
public class IntegrationConnectorExecution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "connector_code", nullable = false)
    @NotNull
    @Size(max = 100)
    private String connectorCode;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status;

    @Column(name = "started_at", nullable = false)
    @NotNull
    private LocalDateTime startedAt;

    @Column(name = "completed_at", nullable = false)
    @NotNull
    private LocalDateTime completedAt;

    @Column(name = "payload_sent", columnDefinition = "TEXT")
    private String payloadSent;

    @Column(name = "payload_received", columnDefinition = "TEXT")
    private String payloadReceived;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

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
     * Retrieves started at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getStartedAt() { return startedAt; }
    /**
     * Performs the setStartedAt operation in this module.
     *
     * @param startedAt the startedAt input value
     */
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
    /**
     * Retrieves completed at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCompletedAt() { return completedAt; }
    /**
     * Performs the setCompletedAt operation in this module.
     *
     * @param completedAt the completedAt input value
     */
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    /**
     * Retrieves payload sent data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPayloadSent() { return payloadSent; }
    /**
     * Performs the setPayloadSent operation in this module.
     *
     * @param payloadSent the payloadSent input value
     */
    public void setPayloadSent(String payloadSent) { this.payloadSent = payloadSent; }
    /**
     * Retrieves payload received data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPayloadReceived() { return payloadReceived; }
    /**
     * Performs the setPayloadReceived operation in this module.
     *
     * @param payloadReceived the payloadReceived input value
     */
    public void setPayloadReceived(String payloadReceived) { this.payloadReceived = payloadReceived; }
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