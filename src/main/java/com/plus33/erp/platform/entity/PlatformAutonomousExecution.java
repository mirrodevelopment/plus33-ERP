/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformAutonomousExecution.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformAutonomousExecutionController
 * Related Service   : PlatformAutonomousExecutionService, PlatformAutonomousExecutionServiceImpl
 * Related Repository: PlatformAutonomousExecutionRepository
 * Related Entity    : PlatformAutonomousExecution
 * Related DTO       : N/A
 * Related Mapper    : PlatformAutonomousExecutionMapper
 * Related DB Table  : platform_autonomous_execution
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformAutonomousExecutionRepository, PlatformAutonomousExecutionMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_autonomous_execution'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformAutonomousExecution}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_autonomous_execution'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_autonomous_execution}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_autonomous_execution")
public class PlatformAutonomousExecution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "action_id", nullable = false)
    @NotNull
    private Long actionId;

    @Column(name = "confidence_score", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal confidenceScore;

    @Column(name = "decision_policy", nullable = false)
    @NotNull
    @Size(max = 100)
    private String decisionPolicy;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "PENDING";

    @Column(name = "operator_notes", columnDefinition = "TEXT")
    private String operatorNotes;

    @Column(name = "executed_at")
    private LocalDateTime executedAt;

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
     * Retrieves action id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getActionId() { return actionId; }
    /**
     * Performs the setActionId operation in this module.
     *
     * @param actionId the actionId input value
     */
    public void setActionId(Long actionId) { this.actionId = actionId; }
    /**
     * Retrieves confidence score data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getConfidenceScore() { return confidenceScore; }
    /**
     * Performs the setConfidenceScore operation in this module.
     *
     * @param confidenceScore the confidenceScore input value
     */
    public void setConfidenceScore(BigDecimal confidenceScore) { this.confidenceScore = confidenceScore; }
    /**
     * Retrieves decision policy data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDecisionPolicy() { return decisionPolicy; }
    /**
     * Performs the setDecisionPolicy operation in this module.
     *
     * @param decisionPolicy the decisionPolicy input value
     */
    public void setDecisionPolicy(String decisionPolicy) { this.decisionPolicy = decisionPolicy; }
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
     * Retrieves operator notes data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getOperatorNotes() { return operatorNotes; }
    /**
     * Performs the setOperatorNotes operation in this module.
     *
     * @param operatorNotes the operatorNotes input value
     */
    public void setOperatorNotes(String operatorNotes) { this.operatorNotes = operatorNotes; }
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