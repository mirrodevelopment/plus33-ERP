/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformAgentToolExecution.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformAgentToolExecutionController
 * Related Service   : PlatformAgentToolExecutionService, PlatformAgentToolExecutionServiceImpl
 * Related Repository: PlatformAgentToolExecutionRepository
 * Related Entity    : PlatformAgentToolExecution
 * Related DTO       : getOutputResponse, outputResponse, setOutputResponse
 * Related Mapper    : PlatformAgentToolExecutionMapper
 * Related DB Table  : platform_agent_tool_execution
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformAgentToolExecutionRepository, PlatformAgentToolExecutionMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_agent_tool_execution'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformAgentToolExecution}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_agent_tool_execution'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_agent_tool_execution}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_agent_tool_execution")
public class PlatformAgentToolExecution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tool_id", nullable = false)
    @NotNull
    private Long toolId;

    @Column(name = "executor_node", nullable = false)
    @NotNull
    @Size(max = 100)
    private String executorNode;

    @Column(name = "input_parameters", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String inputParameters;

    @Column(name = "output_response", columnDefinition = "TEXT")
    private String outputResponse;

    @Column(nullable = false)
    @NotNull
    private Boolean success;

    @Column(name = "elapsed_ms", nullable = false)
    @NotNull
    private Long elapsedMs;

    @Column(name = "executed_at", nullable = false)
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
     * Retrieves tool id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getToolId() { return toolId; }
    /**
     * Performs the setToolId operation in this module.
     *
     * @param toolId the toolId input value
     */
    public void setToolId(Long toolId) { this.toolId = toolId; }
    /**
     * Retrieves executor node data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getExecutorNode() { return executorNode; }
    /**
     * Performs the setExecutorNode operation in this module.
     *
     * @param executorNode the executorNode input value
     */
    public void setExecutorNode(String executorNode) { this.executorNode = executorNode; }
    /**
     * Retrieves input parameters data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getInputParameters() { return inputParameters; }
    /**
     * Performs the setInputParameters operation in this module.
     *
     * @param inputParameters the inputParameters input value
     */
    public void setInputParameters(String inputParameters) { this.inputParameters = inputParameters; }
    /**
     * Retrieves output response data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getOutputResponse() { return outputResponse; }
    /**
     * Performs the setOutputResponse operation in this module.
     *
     * @param outputResponse the outputResponse input value
     */
    public void setOutputResponse(String outputResponse) { this.outputResponse = outputResponse; }
    /**
     * Retrieves success data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getSuccess() { return success; }
    /**
     * Performs the setSuccess operation in this module.
     *
     * @param success the success input value
     */
    public void setSuccess(Boolean success) { this.success = success; }
    /**
     * Retrieves elapsed ms data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getElapsedMs() { return elapsedMs; }
    /**
     * Performs the setElapsedMs operation in this module.
     *
     * @param elapsedMs the elapsedMs input value
     */
    public void setElapsedMs(Long elapsedMs) { this.elapsedMs = elapsedMs; }
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