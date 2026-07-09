/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformAgentReasoning.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformAgentReasoningController
 * Related Service   : PlatformAgentReasoningService, PlatformAgentReasoningServiceImpl
 * Related Repository: PlatformAgentReasoningRepository
 * Related Entity    : PlatformAgentReasoning
 * Related DTO       : N/A
 * Related Mapper    : PlatformAgentReasoningMapper
 * Related DB Table  : platform_agent_reasoning
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformAgentReasoningRepository, PlatformAgentReasoningMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_agent_reasoning'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformAgentReasoning}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_agent_reasoning'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_agent_reasoning}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_agent_reasoning")
public class PlatformAgentReasoning {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message_id", nullable = false)
    @NotNull
    private Long messageId;

    @Column(name = "thought_process", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String thoughtProcess;

    @Column(name = "confidence_score", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal confidenceScore;

    @Column(name = "created_at", nullable = false)
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
     * Retrieves message id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getMessageId() { return messageId; }
    /**
     * Performs the setMessageId operation in this module.
     *
     * @param messageId the messageId input value
     */
    public void setMessageId(Long messageId) { this.messageId = messageId; }
    /**
     * Retrieves thought process data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getThoughtProcess() { return thoughtProcess; }
    /**
     * Performs the setThoughtProcess operation in this module.
     *
     * @param thoughtProcess the thoughtProcess input value
     */
    public void setThoughtProcess(String thoughtProcess) { this.thoughtProcess = thoughtProcess; }
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