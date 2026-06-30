package com.plus33.erp.manufacturing.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "routing_operations")
public class RoutingOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routing_header_id", nullable = false)
    private RoutingHeader routingHeader;

    @Column(name = "operation_number", nullable = false)
    private Integer operationNumber;

    @Column(name = "operation_code", nullable = false, length = 50)
    private String operationCode;

    @Column(length = 255)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_center_id", nullable = false)
    private WorkCenter workCenter;

    @Column(name = "machine_id")
    private Long machineId;

    @Column(name = "labor_group_id")
    private Long laborGroupId;

    @Column(name = "setup_time_hours", nullable = false, precision = 10, scale = 4)
    private BigDecimal setupTimeHours = BigDecimal.ZERO;

    @Column(name = "run_time_per_unit_hours", nullable = false, precision = 10, scale = 6)
    private BigDecimal runTimePerUnitHours = BigDecimal.ZERO;

    @Column(name = "queue_time_hours", nullable = false, precision = 10, scale = 2)
    private BigDecimal queueTimeHours = BigDecimal.ZERO;

    @Column(name = "move_time_hours", nullable = false, precision = 10, scale = 2)
    private BigDecimal moveTimeHours = BigDecimal.ZERO;

    @Column(name = "transfer_batch_size", precision = 10, scale = 2)
    private BigDecimal transferBatchSize;

    @Column(name = "tool_requirements", columnDefinition = "TEXT")
    private String toolRequirements;

    @Column(name = "skill_requirements", columnDefinition = "TEXT")
    private String skillRequirements;

    @Column(name = "instruction_notes", columnDefinition = "TEXT")
    private String instructionNotes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public RoutingOperation() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public RoutingHeader getRoutingHeader() { return routingHeader; }
    public void setRoutingHeader(RoutingHeader routingHeader) { this.routingHeader = routingHeader; }
    public Integer getOperationNumber() { return operationNumber; }
    public void setOperationNumber(Integer operationNumber) { this.operationNumber = operationNumber; }
    public String getOperationCode() { return operationCode; }
    public void setOperationCode(String operationCode) { this.operationCode = operationCode; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public WorkCenter getWorkCenter() { return workCenter; }
    public void setWorkCenter(WorkCenter workCenter) { this.workCenter = workCenter; }
    public Long getMachineId() { return machineId; }
    public void setMachineId(Long machineId) { this.machineId = machineId; }
    public Long getLaborGroupId() { return laborGroupId; }
    public void setLaborGroupId(Long laborGroupId) { this.laborGroupId = laborGroupId; }
    public BigDecimal getSetupTimeHours() { return setupTimeHours; }
    public void setSetupTimeHours(BigDecimal setupTimeHours) { this.setupTimeHours = setupTimeHours; }
    public BigDecimal getRunTimePerUnitHours() { return runTimePerUnitHours; }
    public void setRunTimePerUnitHours(BigDecimal runTimePerUnitHours) { this.runTimePerUnitHours = runTimePerUnitHours; }
    public BigDecimal getQueueTimeHours() { return queueTimeHours; }
    public void setQueueTimeHours(BigDecimal queueTimeHours) { this.queueTimeHours = queueTimeHours; }
    public BigDecimal getMoveTimeHours() { return moveTimeHours; }
    public void setMoveTimeHours(BigDecimal moveTimeHours) { this.moveTimeHours = moveTimeHours; }
    public BigDecimal getTransferBatchSize() { return transferBatchSize; }
    public void setTransferBatchSize(BigDecimal transferBatchSize) { this.transferBatchSize = transferBatchSize; }
    public String getToolRequirements() { return toolRequirements; }
    public void setToolRequirements(String toolRequirements) { this.toolRequirements = toolRequirements; }
    public String getSkillRequirements() { return skillRequirements; }
    public void setSkillRequirements(String skillRequirements) { this.skillRequirements = skillRequirements; }
    public String getInstructionNotes() { return instructionNotes; }
    public void setInstructionNotes(String instructionNotes) { this.instructionNotes = instructionNotes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
