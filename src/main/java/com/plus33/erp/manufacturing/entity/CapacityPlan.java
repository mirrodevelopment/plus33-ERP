/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.entity
 * File              : CapacityPlan.java
 * Purpose           : JPA Entity representing a persistent database record in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CapacityPlanController
 * Related Service   : CapacityPlanService, CapacityPlanServiceImpl
 * Related Repository: CapacityPlanRepository
 * Related Entity    : CapacityPlan
 * Related DTO       : N/A
 * Related Mapper    : CapacityPlanMapper
 * Related DB Table  : capacity_plans
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CapacityPlanRepository, CapacityPlanMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'capacity_plans'. Defines persistent domain object for Manufacturing Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.manufacturing.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code CapacityPlan}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'capacity_plans'.</p>
 *
 * <p><b>Database Table   :</b> {@code capacity_plans}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "capacity_plans")
public class CapacityPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mrp_run_id", nullable = false)
    private MrpRun mrpRun;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_center_id", nullable = false)
    private WorkCenter workCenter;

    @Column(name = "planning_date", nullable = false)
    private LocalDate planningDate;

    @Column(name = "available_hours", nullable = false, precision = 10, scale = 2)
    private BigDecimal availableHours = BigDecimal.ZERO;

    @Column(name = "required_hours", nullable = false, precision = 10, scale = 2)
    private BigDecimal requiredHours = BigDecimal.ZERO;

    @Column(nullable = false)
    private Boolean overloaded = false;

    @Column(name = "machine_utilization_pct", precision = 7, scale = 4)
    private BigDecimal machineUtilizationPct;

    @Column(name = "labor_utilization_pct", precision = 7, scale = 4)
    private BigDecimal laborUtilizationPct;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public CapacityPlan() {}

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
     * Retrieves mrp run data from the database.
     *
     * @return the MrpRun result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public MrpRun getMrpRun() { return mrpRun; }
    /**
     * Performs the setMrpRun operation in this module.
     *
     * @param mrpRun the mrpRun input value
     */
    public void setMrpRun(MrpRun mrpRun) { this.mrpRun = mrpRun; }
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
     * Retrieves planning date data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getPlanningDate() { return planningDate; }
    /**
     * Performs the setPlanningDate operation in this module.
     *
     * @param planningDate the planningDate input value
     */
    public void setPlanningDate(LocalDate planningDate) { this.planningDate = planningDate; }
    /**
     * Retrieves available hours data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getAvailableHours() { return availableHours; }
    /**
     * Performs the setAvailableHours operation in this module.
     *
     * @param availableHours the availableHours input value
     */
    public void setAvailableHours(BigDecimal availableHours) { this.availableHours = availableHours; }
    /**
     * Retrieves required hours data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getRequiredHours() { return requiredHours; }
    /**
     * Performs the setRequiredHours operation in this module.
     *
     * @param requiredHours the requiredHours input value
     */
    public void setRequiredHours(BigDecimal requiredHours) { this.requiredHours = requiredHours; }
    /**
     * Retrieves overloaded data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getOverloaded() { return overloaded; }
    /**
     * Performs the setOverloaded operation in this module.
     *
     * @param overloaded the overloaded input value
     */
    public void setOverloaded(Boolean overloaded) { this.overloaded = overloaded; }
    /**
     * Retrieves machine utilization pct data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getMachineUtilizationPct() { return machineUtilizationPct; }
    /**
     * Performs the setMachineUtilizationPct operation in this module.
     *
     * @param machineUtilizationPct the machineUtilizationPct input value
     */
    public void setMachineUtilizationPct(BigDecimal machineUtilizationPct) { this.machineUtilizationPct = machineUtilizationPct; }
    /**
     * Retrieves labor utilization pct data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getLaborUtilizationPct() { return laborUtilizationPct; }
    /**
     * Performs the setLaborUtilizationPct operation in this module.
     *
     * @param laborUtilizationPct the laborUtilizationPct input value
     */
    public void setLaborUtilizationPct(BigDecimal laborUtilizationPct) { this.laborUtilizationPct = laborUtilizationPct; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
}