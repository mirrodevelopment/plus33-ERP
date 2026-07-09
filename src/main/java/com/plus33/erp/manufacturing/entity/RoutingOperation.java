/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.entity
 * File              : RoutingOperation.java
 * Purpose           : JPA Entity representing a persistent database record in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: RoutingOperationController
 * Related Service   : RoutingOperationService, RoutingOperationServiceImpl
 * Related Repository: RoutingOperationRepository
 * Related Entity    : RoutingOperation
 * Related DTO       : N/A
 * Related Mapper    : RoutingOperationMapper
 * Related DB Table  : routing_operations
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : RoutingOperationRepository, RoutingOperationMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'routing_operations'. Defines persistent domain object for Manufacturing Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.manufacturing.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code RoutingOperation}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'routing_operations'.</p>
 *
 * <p><b>Database Table   :</b> {@code routing_operations}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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
     * Retrieves routing header data from the database.
     *
     * @return the RoutingHeader result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public RoutingHeader getRoutingHeader() { return routingHeader; }
    /**
     * Performs the setRoutingHeader operation in this module.
     *
     * @param routingHeader the routingHeader input value
     */
    public void setRoutingHeader(RoutingHeader routingHeader) { this.routingHeader = routingHeader; }
    /**
     * Retrieves operation number data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getOperationNumber() { return operationNumber; }
    /**
     * Performs the setOperationNumber operation in this module.
     *
     * @param operationNumber the operationNumber input value
     */
    public void setOperationNumber(Integer operationNumber) { this.operationNumber = operationNumber; }
    /**
     * Retrieves operation code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getOperationCode() { return operationCode; }
    /**
     * Performs the setOperationCode operation in this module.
     *
     * @param operationCode the operationCode input value
     */
    public void setOperationCode(String operationCode) { this.operationCode = operationCode; }
    /**
     * Retrieves description data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDescription() { return description; }
    /**
     * Performs the setDescription operation in this module.
     *
     * @param description the description input value
     */
    public void setDescription(String description) { this.description = description; }
    /**
     * Retrieves work center data from the database.
     *
     * @return the WorkCenter result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public WorkCenter getWorkCenter() { return workCenter; }
    /**
     * Performs the setWorkCenter operation in this module.
     *
     * @param workCenter the workCenter input value
     */
    public void setWorkCenter(WorkCenter workCenter) { this.workCenter = workCenter; }
    /**
     * Retrieves machine id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getMachineId() { return machineId; }
    /**
     * Performs the setMachineId operation in this module.
     *
     * @param machineId the machineId input value
     */
    public void setMachineId(Long machineId) { this.machineId = machineId; }
    /**
     * Retrieves labor group id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getLaborGroupId() { return laborGroupId; }
    /**
     * Performs the setLaborGroupId operation in this module.
     *
     * @param laborGroupId the laborGroupId input value
     */
    public void setLaborGroupId(Long laborGroupId) { this.laborGroupId = laborGroupId; }
    /**
     * Retrieves setup time hours data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getSetupTimeHours() { return setupTimeHours; }
    /**
     * Performs the setSetupTimeHours operation in this module.
     *
     * @param setupTimeHours the setupTimeHours input value
     */
    public void setSetupTimeHours(BigDecimal setupTimeHours) { this.setupTimeHours = setupTimeHours; }
    /**
     * Retrieves run time per unit hours data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getRunTimePerUnitHours() { return runTimePerUnitHours; }
    /**
     * Performs the setRunTimePerUnitHours operation in this module.
     *
     * @param runTimePerUnitHours the runTimePerUnitHours input value
     */
    public void setRunTimePerUnitHours(BigDecimal runTimePerUnitHours) { this.runTimePerUnitHours = runTimePerUnitHours; }
    /**
     * Retrieves queue time hours data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getQueueTimeHours() { return queueTimeHours; }
    /**
     * Performs the setQueueTimeHours operation in this module.
     *
     * @param queueTimeHours the queueTimeHours input value
     */
    public void setQueueTimeHours(BigDecimal queueTimeHours) { this.queueTimeHours = queueTimeHours; }
    /**
     * Retrieves move time hours data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getMoveTimeHours() { return moveTimeHours; }
    /**
     * Performs the setMoveTimeHours operation in this module.
     *
     * @param moveTimeHours the moveTimeHours input value
     */
    public void setMoveTimeHours(BigDecimal moveTimeHours) { this.moveTimeHours = moveTimeHours; }
    /**
     * Retrieves transfer batch size data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getTransferBatchSize() { return transferBatchSize; }
    /**
     * Performs the setTransferBatchSize operation in this module.
     *
     * @param transferBatchSize the transferBatchSize input value
     */
    public void setTransferBatchSize(BigDecimal transferBatchSize) { this.transferBatchSize = transferBatchSize; }
    /**
     * Retrieves tool requirements data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getToolRequirements() { return toolRequirements; }
    /**
     * Performs the setToolRequirements operation in this module.
     *
     * @param toolRequirements the toolRequirements input value
     */
    public void setToolRequirements(String toolRequirements) { this.toolRequirements = toolRequirements; }
    /**
     * Retrieves skill requirements data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSkillRequirements() { return skillRequirements; }
    /**
     * Performs the setSkillRequirements operation in this module.
     *
     * @param skillRequirements the skillRequirements input value
     */
    public void setSkillRequirements(String skillRequirements) { this.skillRequirements = skillRequirements; }
    /**
     * Retrieves instruction notes data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getInstructionNotes() { return instructionNotes; }
    /**
     * Performs the setInstructionNotes operation in this module.
     *
     * @param instructionNotes the instructionNotes input value
     */
    public void setInstructionNotes(String instructionNotes) { this.instructionNotes = instructionNotes; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
}