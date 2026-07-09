/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformScadaWriteCommand.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformScadaWriteCommandController
 * Related Service   : PlatformScadaWriteCommandService, PlatformScadaWriteCommandServiceImpl
 * Related Repository: PlatformScadaWriteCommandRepository
 * Related Entity    : PlatformScadaWriteCommand
 * Related DTO       : N/A
 * Related Mapper    : PlatformScadaWriteCommandMapper
 * Related DB Table  : platform_scada_write_command
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformScadaWriteCommandRepository, PlatformScadaWriteCommandMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_scada_write_command'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
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
 * <p><b>Class  :</b> {@code PlatformScadaWriteCommand}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_scada_write_command'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_scada_write_command}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_scada_write_command")
public class PlatformScadaWriteCommand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_id", nullable = false)
    @NotNull
    private Long deviceId;

    @Column(name = "register_id", nullable = false)
    @NotNull
    private Long registerId;

    @Column(name = "command_value", nullable = false, precision = 19, scale = 4)
    @NotNull
    private BigDecimal commandValue;

    @Column(name = "command_hash", nullable = false)
    @NotNull
    @Size(max = 150)
    private String commandHash;

    @Column(name = "approved_by", nullable = false)
    @NotNull
    @Size(max = 100)
    private String approvedBy;

    @Column(name = "executed_by", nullable = false)
    @NotNull
    @Size(max = 100)
    private String executedBy;

    @Column(nullable = false)
    @NotNull
    @Size(max = 250)
    private String signature;

    @Column(name = "execution_time", nullable = false)
    @NotNull
    private LocalDateTime executionTime = LocalDateTime.now();

    @Column(name = "rollback_supported", nullable = false)
    @NotNull
    private Boolean rollbackSupported = false;

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
     * Retrieves device id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getDeviceId() { return deviceId; }
    /**
     * Performs the setDeviceId operation in this module.
     *
     * @param deviceId the deviceId input value
     */
    public void setDeviceId(Long deviceId) { this.deviceId = deviceId; }
    /**
     * Retrieves register id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getRegisterId() { return registerId; }
    /**
     * Performs the setRegisterId operation in this module.
     *
     * @param registerId the registerId input value
     */
    public void setRegisterId(Long registerId) { this.registerId = registerId; }
    /**
     * Retrieves command value data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getCommandValue() { return commandValue; }
    /**
     * Performs the setCommandValue operation in this module.
     *
     * @param commandValue the commandValue input value
     */
    public void setCommandValue(BigDecimal commandValue) { this.commandValue = commandValue; }
    /**
     * Retrieves command hash data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCommandHash() { return commandHash; }
    /**
     * Performs the setCommandHash operation in this module.
     *
     * @param commandHash the commandHash input value
     */
    public void setCommandHash(String commandHash) { this.commandHash = commandHash; }
    /**
     * Retrieves approved by data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getApprovedBy() { return approvedBy; }
    /**
     * Performs the setApprovedBy operation in this module.
     *
     * @param approvedBy the approvedBy input value
     */
    public void setApprovedBy(String approvedBy) { this.approvedBy = approvedBy; }
    /**
     * Retrieves executed by data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getExecutedBy() { return executedBy; }
    /**
     * Performs the setExecutedBy operation in this module.
     *
     * @param executedBy the executedBy input value
     */
    public void setExecutedBy(String executedBy) { this.executedBy = executedBy; }
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
     * Retrieves execution time data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getExecutionTime() { return executionTime; }
    /**
     * Performs the setExecutionTime operation in this module.
     *
     * @param executionTime the executionTime input value
     */
    public void setExecutionTime(LocalDateTime executionTime) { this.executionTime = executionTime; }
    /**
     * Retrieves rollback supported data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getRollbackSupported() { return rollbackSupported; }
    /**
     * Performs the setRollbackSupported operation in this module.
     *
     * @param rollbackSupported the rollbackSupported input value
     */
    public void setRollbackSupported(Boolean rollbackSupported) { this.rollbackSupported = rollbackSupported; }
}