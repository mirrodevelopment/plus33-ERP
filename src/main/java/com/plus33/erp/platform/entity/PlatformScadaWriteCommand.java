package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getDeviceId() { return deviceId; }
    public void setDeviceId(Long deviceId) { this.deviceId = deviceId; }
    public Long getRegisterId() { return registerId; }
    public void setRegisterId(Long registerId) { this.registerId = registerId; }
    public BigDecimal getCommandValue() { return commandValue; }
    public void setCommandValue(BigDecimal commandValue) { this.commandValue = commandValue; }
    public String getCommandHash() { return commandHash; }
    public void setCommandHash(String commandHash) { this.commandHash = commandHash; }
    public String getApprovedBy() { return approvedBy; }
    public void setApprovedBy(String approvedBy) { this.approvedBy = approvedBy; }
    public String getExecutedBy() { return executedBy; }
    public void setExecutedBy(String executedBy) { this.executedBy = executedBy; }
    public String getSignature() { return signature; }
    public void setSignature(String signature) { this.signature = signature; }
    public LocalDateTime getExecutionTime() { return executionTime; }
    public void setExecutionTime(LocalDateTime executionTime) { this.executionTime = executionTime; }
    public Boolean getRollbackSupported() { return rollbackSupported; }
    public void setRollbackSupported(Boolean rollbackSupported) { this.rollbackSupported = rollbackSupported; }
}