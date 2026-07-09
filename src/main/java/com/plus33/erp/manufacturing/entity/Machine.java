/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.entity
 * File              : Machine.java
 * Purpose           : JPA Entity representing a persistent database record in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: MachineController
 * Related Service   : MachineService, MachineServiceImpl
 * Related Repository: MachineRepository
 * Related Entity    : Machine
 * Related DTO       : N/A
 * Related Mapper    : MachineMapper
 * Related DB Table  : machines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : MachineRepository, MachineMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'machines'. Defines persistent domain object for Manufacturing Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.manufacturing.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code Machine}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'machines'.</p>
 *
 * <p><b>Database Table   :</b> {@code machines}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "machines")
public class Machine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_center_id", nullable = false)
    private WorkCenter workCenter;

    @Column(name = "asset_id")
    private Long assetId;

    @Column(nullable = false, length = 50)
    private String code;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(name = "machine_type", length = 50)
    private String machineType;

    @Column(name = "max_speed_units", precision = 10, scale = 2)
    private BigDecimal maxSpeedUnits;

    @Column(name = "cycle_time_seconds", precision = 10, scale = 2)
    private BigDecimal cycleTimeSeconds;

    @Column(name = "setup_time_minutes", nullable = false, precision = 10, scale = 2)
    private BigDecimal setupTimeMinutes = BigDecimal.ZERO;

    @Column(name = "hourly_run_cost", nullable = false, precision = 18, scale = 4)
    private BigDecimal hourlyRunCost = BigDecimal.ZERO;

    @Column(name = "hourly_idle_cost", nullable = false, precision = 18, scale = 4)
    private BigDecimal hourlyIdleCost = BigDecimal.ZERO;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Machine() {}

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
     * Retrieves asset id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getAssetId() { return assetId; }
    /**
     * Performs the setAssetId operation in this module.
     *
     * @param assetId the assetId input value
     */
    public void setAssetId(Long assetId) { this.assetId = assetId; }
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
     * Retrieves machine type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getMachineType() { return machineType; }
    /**
     * Performs the setMachineType operation in this module.
     *
     * @param machineType the machineType input value
     */
    public void setMachineType(String machineType) { this.machineType = machineType; }
    /**
     * Retrieves max speed units data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getMaxSpeedUnits() { return maxSpeedUnits; }
    /**
     * Performs the setMaxSpeedUnits operation in this module.
     *
     * @param maxSpeedUnits the maxSpeedUnits input value
     */
    public void setMaxSpeedUnits(BigDecimal maxSpeedUnits) { this.maxSpeedUnits = maxSpeedUnits; }
    /**
     * Retrieves cycle time seconds data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getCycleTimeSeconds() { return cycleTimeSeconds; }
    /**
     * Performs the setCycleTimeSeconds operation in this module.
     *
     * @param cycleTimeSeconds the cycleTimeSeconds input value
     */
    public void setCycleTimeSeconds(BigDecimal cycleTimeSeconds) { this.cycleTimeSeconds = cycleTimeSeconds; }
    /**
     * Retrieves setup time minutes data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getSetupTimeMinutes() { return setupTimeMinutes; }
    /**
     * Performs the setSetupTimeMinutes operation in this module.
     *
     * @param setupTimeMinutes the setupTimeMinutes input value
     */
    public void setSetupTimeMinutes(BigDecimal setupTimeMinutes) { this.setupTimeMinutes = setupTimeMinutes; }
    /**
     * Retrieves hourly run cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getHourlyRunCost() { return hourlyRunCost; }
    /**
     * Performs the setHourlyRunCost operation in this module.
     *
     * @param hourlyRunCost the hourlyRunCost input value
     */
    public void setHourlyRunCost(BigDecimal hourlyRunCost) { this.hourlyRunCost = hourlyRunCost; }
    /**
     * Retrieves hourly idle cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getHourlyIdleCost() { return hourlyIdleCost; }
    /**
     * Performs the setHourlyIdleCost operation in this module.
     *
     * @param hourlyIdleCost the hourlyIdleCost input value
     */
    public void setHourlyIdleCost(BigDecimal hourlyIdleCost) { this.hourlyIdleCost = hourlyIdleCost; }
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
}