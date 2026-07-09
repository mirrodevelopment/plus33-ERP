/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformEvEnergyAuditLog.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformEvEnergyAuditLogController
 * Related Service   : PlatformEvEnergyAuditLogService, PlatformEvEnergyAuditLogServiceImpl
 * Related Repository: PlatformEvEnergyAuditLogRepository
 * Related Entity    : PlatformEvEnergyAuditLog
 * Related DTO       : N/A
 * Related Mapper    : PlatformEvEnergyAuditLogMapper
 * Related DB Table  : platform_ev_energy_audit_log
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformEvEnergyAuditLogRepository, PlatformEvEnergyAuditLogMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_ev_energy_audit_log'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
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
 * <p><b>Class  :</b> {@code PlatformEvEnergyAuditLog}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_ev_energy_audit_log'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_ev_energy_audit_log}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_ev_energy_audit_log")
public class PlatformEvEnergyAuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "optimization_algorithm", nullable = false)
    @NotNull
    @Size(max = 100)
    private String optimizationAlgorithm;

    @Column(name = "energy_before_kwh", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal energyBeforeKwh;

    @Column(name = "energy_after_kwh", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal energyAfterKwh;

    @Column(name = "estimated_cost", nullable = false, precision = 12, scale = 2)
    @NotNull
    private BigDecimal estimatedCost;

    @Column(name = "estimated_savings", nullable = false, precision = 12, scale = 2)
    @NotNull
    private BigDecimal estimatedSavings;

    @Column(name = "carbon_offset_kg", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal carbonOffsetKg;

    @Column(nullable = false)
    @NotNull
    @Size(max = 100)
    private String operator;

    @Column(name = "trace_id", nullable = false)
    @NotNull
    @Size(max = 100)
    private String traceId;

    @Column(name = "execution_duration_ms", nullable = false)
    @NotNull
    private Long executionDurationMs;

    @Column(name = "audited_at", nullable = false)
    @NotNull
    private LocalDateTime auditedAt = LocalDateTime.now();

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
     * Retrieves optimization algorithm data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getOptimizationAlgorithm() { return optimizationAlgorithm; }
    /**
     * Performs the setOptimizationAlgorithm operation in this module.
     *
     * @param optimizationAlgorithm the optimizationAlgorithm input value
     */
    public void setOptimizationAlgorithm(String optimizationAlgorithm) { this.optimizationAlgorithm = optimizationAlgorithm; }
    /**
     * Retrieves energy before kwh data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getEnergyBeforeKwh() { return energyBeforeKwh; }
    /**
     * Performs the setEnergyBeforeKwh operation in this module.
     *
     * @param energyBeforeKwh the energyBeforeKwh input value
     */
    public void setEnergyBeforeKwh(BigDecimal energyBeforeKwh) { this.energyBeforeKwh = energyBeforeKwh; }
    /**
     * Retrieves energy after kwh data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getEnergyAfterKwh() { return energyAfterKwh; }
    /**
     * Performs the setEnergyAfterKwh operation in this module.
     *
     * @param energyAfterKwh the energyAfterKwh input value
     */
    public void setEnergyAfterKwh(BigDecimal energyAfterKwh) { this.energyAfterKwh = energyAfterKwh; }
    /**
     * Retrieves estimated cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getEstimatedCost() { return estimatedCost; }
    /**
     * Performs the setEstimatedCost operation in this module.
     *
     * @param estimatedCost the estimatedCost input value
     */
    public void setEstimatedCost(BigDecimal estimatedCost) { this.estimatedCost = estimatedCost; }
    /**
     * Retrieves estimated savings data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getEstimatedSavings() { return estimatedSavings; }
    /**
     * Performs the setEstimatedSavings operation in this module.
     *
     * @param estimatedSavings the estimatedSavings input value
     */
    public void setEstimatedSavings(BigDecimal estimatedSavings) { this.estimatedSavings = estimatedSavings; }
    /**
     * Retrieves carbon offset kg data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getCarbonOffsetKg() { return carbonOffsetKg; }
    /**
     * Performs the setCarbonOffsetKg operation in this module.
     *
     * @param carbonOffsetKg the carbonOffsetKg input value
     */
    public void setCarbonOffsetKg(BigDecimal carbonOffsetKg) { this.carbonOffsetKg = carbonOffsetKg; }
    /**
     * Retrieves operator data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getOperator() { return operator; }
    /**
     * Performs the setOperator operation in this module.
     *
     * @param operator the operator input value
     */
    public void setOperator(String operator) { this.operator = operator; }
    /**
     * Retrieves trace id data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTraceId() { return traceId; }
    /**
     * Performs the setTraceId operation in this module.
     *
     * @param traceId the traceId input value
     */
    public void setTraceId(String traceId) { this.traceId = traceId; }
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
     * Retrieves audited at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getAuditedAt() { return auditedAt; }
    /**
     * Performs the setAuditedAt operation in this module.
     *
     * @param auditedAt the auditedAt input value
     */
    public void setAuditedAt(LocalDateTime auditedAt) { this.auditedAt = auditedAt; }
}