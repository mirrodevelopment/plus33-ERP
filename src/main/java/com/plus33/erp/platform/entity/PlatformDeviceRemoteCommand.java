/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformDeviceRemoteCommand.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformDeviceRemoteCommandController
 * Related Service   : PlatformDeviceRemoteCommandService, PlatformDeviceRemoteCommandServiceImpl
 * Related Repository: PlatformDeviceRemoteCommandRepository
 * Related Entity    : PlatformDeviceRemoteCommand
 * Related DTO       : N/A
 * Related Mapper    : PlatformDeviceRemoteCommandMapper
 * Related DB Table  : platform_device_remote_command
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformDeviceRemoteCommandRepository, PlatformDeviceRemoteCommandMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_device_remote_command'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformDeviceRemoteCommand}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_device_remote_command'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_device_remote_command}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_device_remote_command")
public class PlatformDeviceRemoteCommand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "node_id", nullable = false)
    @NotNull
    private Long nodeId;

    @Column(name = "command_type", nullable = false)
    @NotNull
    @Size(max = 100)
    private String commandType; // Restart, Shutdown, Sync, CollectLogs, RotateCertificate

    @Column(columnDefinition = "TEXT")
    private String parameters;

    @Column(nullable = false)
    @NotNull
    @Size(max = 256)
    private String signature;

    @Column(name = "timeout_seconds", nullable = false)
    @NotNull
    private Integer timeoutSeconds = 60;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status; // DISPATCHED, EXECUTED, FAILED, TIMED_OUT

    @Column(name = "response_payload", columnDefinition = "TEXT")
    private String responsePayload;

    @Column(name = "exit_code")
    private Integer exitCode;

    @Column(name = "execution_duration_ms")
    private Long executionDurationMs;

    @Column(name = "dispatched_at", nullable = false)
    @NotNull
    private LocalDateTime dispatchedAt = LocalDateTime.now();

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
     * Retrieves node id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getNodeId() { return nodeId; }
    /**
     * Performs the setNodeId operation in this module.
     *
     * @param nodeId the nodeId input value
     */
    public void setNodeId(Long nodeId) { this.nodeId = nodeId; }
    /**
     * Retrieves command type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCommandType() { return commandType; }
    /**
     * Performs the setCommandType operation in this module.
     *
     * @param commandType the commandType input value
     */
    public void setCommandType(String commandType) { this.commandType = commandType; }
    /**
     * Retrieves parameters data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getParameters() { return parameters; }
    /**
     * Performs the setParameters operation in this module.
     *
     * @param parameters the parameters input value
     */
    public void setParameters(String parameters) { this.parameters = parameters; }
    /**
     * Retrieves signature data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSignature() { return signature; }
    /**
     * Performs the setSignature operation in this module.
     *
     * @param signature the signature input value
     */
    public void setSignature(String signature) { this.signature = signature; }
    /**
     * Retrieves timeout seconds data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getTimeoutSeconds() { return timeoutSeconds; }
    /**
     * Performs the setTimeoutSeconds operation in this module.
     *
     * @param timeoutSeconds the timeoutSeconds input value
     */
    public void setTimeoutSeconds(Integer timeoutSeconds) { this.timeoutSeconds = timeoutSeconds; }
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
     * Retrieves response payload data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getResponsePayload() { return responsePayload; }
    /**
     * Performs the setResponsePayload operation in this module.
     *
     * @param responsePayload the responsePayload input value
     */
    public void setResponsePayload(String responsePayload) { this.responsePayload = responsePayload; }
    /**
     * Retrieves exit code data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getExitCode() { return exitCode; }
    /**
     * Performs the setExitCode operation in this module.
     *
     * @param exitCode the exitCode input value
     */
    public void setExitCode(Integer exitCode) { this.exitCode = exitCode; }
    /**
     * Retrieves execution duration ms data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getExecutionDurationMs() { return executionDurationMs; }
    /**
     * Performs the setExecutionDurationMs operation in this module.
     *
     * @param executionDurationMs the executionDurationMs input value
     */
    public void setExecutionDurationMs(Long executionDurationMs) { this.executionDurationMs = executionDurationMs; }
    /**
     * Retrieves dispatched at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getDispatchedAt() { return dispatchedAt; }
    /**
     * Performs the setDispatchedAt operation in this module.
     *
     * @param dispatchedAt the dispatchedAt input value
     */
    public void setDispatchedAt(LocalDateTime dispatchedAt) { this.dispatchedAt = dispatchedAt; }
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