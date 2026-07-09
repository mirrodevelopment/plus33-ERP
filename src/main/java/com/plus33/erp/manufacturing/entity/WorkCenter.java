/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.entity
 * File              : WorkCenter.java
 * Purpose           : JPA Entity representing a persistent database record in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WorkCenterController
 * Related Service   : WorkCenterService, WorkCenterServiceImpl
 * Related Repository: WorkCenterRepository
 * Related Entity    : WorkCenter
 * Related DTO       : N/A
 * Related Mapper    : WorkCenterMapper
 * Related DB Table  : work_centers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : WorkCenterRepository, WorkCenterMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'work_centers'. Defines persistent domain object for Manufacturing Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.manufacturing.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code WorkCenter}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'work_centers'.</p>
 *
 * <p><b>Database Table   :</b> {@code work_centers}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "work_centers")
public class WorkCenter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(nullable = false, length = 50)
    private String code;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(length = 100)
    private String department;

    @Column(name = "work_center_type", nullable = false, length = 30)
    private String workCenterType = "MACHINE"; // MACHINE, LABOR, MIXED, SUBCONTRACT

    @Column(name = "machine_capacity", nullable = false, precision = 10, scale = 2)
    private BigDecimal machineCapacity = BigDecimal.ONE;

    @Column(name = "labor_capacity", nullable = false, precision = 10, scale = 2)
    private BigDecimal laborCapacity = BigDecimal.ONE;

    @Column(name = "hourly_machine_rate", nullable = false, precision = 18, scale = 4)
    private BigDecimal hourlyMachineRate = BigDecimal.ZERO;

    @Column(name = "hourly_labor_rate", nullable = false, precision = 18, scale = 4)
    private BigDecimal hourlyLaborRate = BigDecimal.ZERO;

    @Column(name = "overhead_rate", nullable = false, precision = 18, scale = 4)
    private BigDecimal overheadRate = BigDecimal.ZERO;

    @Column(name = "overhead_rate_type", nullable = false, length = 30)
    private String overheadRateType = "MACHINE_HOUR"; // MACHINE_HOUR, LABOR_HOUR, DIRECT_LABOR_COST

    @Column(name = "queue_time_hours", nullable = false, precision = 10, scale = 2)
    private BigDecimal queueTimeHours = BigDecimal.ZERO;

    @Column(name = "move_time_hours", nullable = false, precision = 10, scale = 2)
    private BigDecimal moveTimeHours = BigDecimal.ZERO;

    @Column(name = "efficiency_factor", nullable = false, precision = 7, scale = 4)
    private BigDecimal efficiencyFactor = new BigDecimal("100.00");

    @Column(name = "gl_account_id")
    private Long glAccountId;

    @Column(nullable = false)
    private Boolean active = Boolean.TRUE;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    /**
     * Handles the update event or exception in the business workflow.
     *
     */
    @PreUpdate
    protected void onUpdate() { this.updatedAt = LocalDateTime.now(); }

    public WorkCenter() {}

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
     * Retrieves company id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCompanyId() { return companyId; }
    /**
     * Performs the setCompanyId operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     */
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    /**
     * Retrieves code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCode() { return code; }
    /**
     * Performs the setCode operation in this module.
     *
     * @param code the code input value
     */
    public void setCode(String code) { this.code = code; }
    /**
     * Retrieves name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getName() { return name; }
    /**
     * Performs the setName operation in this module.
     *
     * @param name the name input value
     */
    public void setName(String name) { this.name = name; }
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
     * Retrieves department data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDepartment() { return department; }
    /**
     * Performs the setDepartment operation in this module.
     *
     * @param department the department input value
     */
    public void setDepartment(String department) { this.department = department; }
    /**
     * Retrieves work center type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getWorkCenterType() { return workCenterType; }
    /**
     * Performs the setWorkCenterType operation in this module.
     *
     * @param workCenterType the workCenterType input value
     */
    public void setWorkCenterType(String workCenterType) { this.workCenterType = workCenterType; }
    /**
     * Retrieves machine capacity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getMachineCapacity() { return machineCapacity; }
    /**
     * Performs the setMachineCapacity operation in this module.
     *
     * @param machineCapacity the machineCapacity input value
     */
    public void setMachineCapacity(BigDecimal machineCapacity) { this.machineCapacity = machineCapacity; }
    /**
     * Retrieves labor capacity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getLaborCapacity() { return laborCapacity; }
    /**
     * Performs the setLaborCapacity operation in this module.
     *
     * @param laborCapacity the laborCapacity input value
     */
    public void setLaborCapacity(BigDecimal laborCapacity) { this.laborCapacity = laborCapacity; }
    /**
     * Retrieves hourly machine rate data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getHourlyMachineRate() { return hourlyMachineRate; }
    /**
     * Performs the setHourlyMachineRate operation in this module.
     *
     * @param hourlyMachineRate the hourlyMachineRate input value
     */
    public void setHourlyMachineRate(BigDecimal hourlyMachineRate) { this.hourlyMachineRate = hourlyMachineRate; }
    /**
     * Retrieves hourly labor rate data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getHourlyLaborRate() { return hourlyLaborRate; }
    /**
     * Performs the setHourlyLaborRate operation in this module.
     *
     * @param hourlyLaborRate the hourlyLaborRate input value
     */
    public void setHourlyLaborRate(BigDecimal hourlyLaborRate) { this.hourlyLaborRate = hourlyLaborRate; }
    /**
     * Retrieves overhead rate data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getOverheadRate() { return overheadRate; }
    /**
     * Performs the setOverheadRate operation in this module.
     *
     * @param overheadRate the overheadRate input value
     */
    public void setOverheadRate(BigDecimal overheadRate) { this.overheadRate = overheadRate; }
    /**
     * Retrieves overhead rate type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getOverheadRateType() { return overheadRateType; }
    /**
     * Performs the setOverheadRateType operation in this module.
     *
     * @param overheadRateType the overheadRateType input value
     */
    public void setOverheadRateType(String overheadRateType) { this.overheadRateType = overheadRateType; }
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
     * Retrieves efficiency factor data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getEfficiencyFactor() { return efficiencyFactor; }
    /**
     * Performs the setEfficiencyFactor operation in this module.
     *
     * @param efficiencyFactor the efficiencyFactor input value
     */
    public void setEfficiencyFactor(BigDecimal efficiencyFactor) { this.efficiencyFactor = efficiencyFactor; }
    /**
     * Retrieves gl account id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getGlAccountId() { return glAccountId; }
    /**
     * Performs the setGlAccountId operation in this module.
     *
     * @param glAccountId the glAccountId input value
     */
    public void setGlAccountId(Long glAccountId) { this.glAccountId = glAccountId; }
    /**
     * Retrieves active data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getActive() { return active; }
    /**
     * Performs the setActive operation in this module.
     *
     * @param active the active input value
     */
    public void setActive(Boolean active) { this.active = active; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
    /**
     * Retrieves updated at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}