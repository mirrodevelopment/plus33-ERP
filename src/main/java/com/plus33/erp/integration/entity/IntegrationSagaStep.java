/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.entity
 * File              : IntegrationSagaStep.java
 * Purpose           : JPA Entity representing a persistent database record in Integration Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: IntegrationSagaStepController
 * Related Service   : IntegrationSagaStepService, IntegrationSagaStepServiceImpl
 * Related Repository: IntegrationSagaStepRepository
 * Related Entity    : IntegrationSagaStep
 * Related DTO       : N/A
 * Related Mapper    : IntegrationSagaStepMapper
 * Related DB Table  : integration_saga_step
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : IntegrationSagaStepRepository, IntegrationSagaStepMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'integration_saga_step'. Defines persistent domain object for Integration Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.integration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Integration Module</b>
 *
 * <p><b>Class  :</b> {@code IntegrationSagaStep}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.integration.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'integration_saga_step'.</p>
 *
 * <p><b>Database Table   :</b> {@code integration_saga_step}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "integration_saga_step")
public class IntegrationSagaStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "saga_code", nullable = false)
    @NotNull
    @Size(max = 100)
    private String sagaCode;

    @Column(name = "step_name", nullable = false)
    @NotNull
    @Size(max = 100)
    private String stepName;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status;

    @Column(name = "action_payload", columnDefinition = "TEXT")
    private String actionPayload;

    @Column(name = "compensation_payload", columnDefinition = "TEXT")
    private String compensationPayload;

    @Column(name = "execution_order", nullable = false)
    @NotNull
    private Integer executionOrder;

    @Column(name = "executed_at", nullable = false, updatable = false)
    @NotNull
    private LocalDateTime executedAt = LocalDateTime.now();

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
     * Retrieves saga code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSagaCode() { return sagaCode; }
    /**
     * Performs the setSagaCode operation in this module.
     *
     * @param sagaCode the sagaCode input value
     */
    public void setSagaCode(String sagaCode) { this.sagaCode = sagaCode; }
    /**
     * Retrieves step name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getStepName() { return stepName; }
    /**
     * Performs the setStepName operation in this module.
     *
     * @param stepName the stepName input value
     */
    public void setStepName(String stepName) { this.stepName = stepName; }
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
     * Retrieves action payload data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getActionPayload() { return actionPayload; }
    /**
     * Performs the setActionPayload operation in this module.
     *
     * @param actionPayload the actionPayload input value
     */
    public void setActionPayload(String actionPayload) { this.actionPayload = actionPayload; }
    /**
     * Retrieves compensation payload data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCompensationPayload() { return compensationPayload; }
    /**
     * Performs the setCompensationPayload operation in this module.
     *
     * @param compensationPayload the compensationPayload input value
     */
    public void setCompensationPayload(String compensationPayload) { this.compensationPayload = compensationPayload; }
    /**
     * Retrieves execution order data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getExecutionOrder() { return executionOrder; }
    /**
     * Performs the setExecutionOrder operation in this module.
     *
     * @param executionOrder the executionOrder input value
     */
    public void setExecutionOrder(Integer executionOrder) { this.executionOrder = executionOrder; }
    /**
     * Retrieves executed at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getExecutedAt() { return executedAt; }
    /**
     * Performs the setExecutedAt operation in this module.
     *
     * @param executedAt the executedAt input value
     */
    public void setExecutedAt(LocalDateTime executedAt) { this.executedAt = executedAt; }
}