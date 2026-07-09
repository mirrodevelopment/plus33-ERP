/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Esm Module
 * Package           : com.plus33.erp.esm.entity
 * File              : PreventiveMaintenancePlan.java
 * Purpose           : JPA Entity representing a persistent database record in Esm Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PreventiveMaintenancePlanController
 * Related Service   : PreventiveMaintenancePlanService, PreventiveMaintenancePlanServiceImpl
 * Related Repository: PreventiveMaintenancePlanRepository
 * Related Entity    : PreventiveMaintenancePlan
 * Related DTO       : N/A
 * Related Mapper    : PreventiveMaintenancePlanMapper
 * Related DB Table  : esm_pm_plans
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PreventiveMaintenancePlanRepository, PreventiveMaintenancePlanMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'esm_pm_plans'. Defines persistent domain object for Esm Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.esm.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * <b>PLUS33 Coffee ERP -- Esm Module</b>
 *
 * <p><b>Class  :</b> {@code PreventiveMaintenancePlan}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.esm.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'esm_pm_plans'.</p>
 *
 * <p><b>Database Table   :</b> {@code esm_pm_plans}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "esm_pm_plans")
public class PreventiveMaintenancePlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "installed_asset_id", nullable = false)
    private Long installedAssetId;

    @Column(name = "interval_days", nullable = false)
    private Integer intervalDays;

    @Column(name = "next_service_date", nullable = false)
    private LocalDate nextServiceDate;

    @Column(name = "trigger_type", nullable = false, length = 30)
    private String triggerType = "CALENDAR";

    @Column(nullable = false)
    private Boolean active = true;

    // Getters and setters
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
     * Retrieves a paginated list of installed asset id records.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getInstalledAssetId() { return installedAssetId; }
    /**
     * Performs the setInstalledAssetId operation in this module.
     *
     * @param installedAssetId the installedAssetId input value
     */
    public void setInstalledAssetId(Long installedAssetId) { this.installedAssetId = installedAssetId; }
    /**
     * Retrieves interval days data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getIntervalDays() { return intervalDays; }
    /**
     * Performs the setIntervalDays operation in this module.
     *
     * @param intervalDays the intervalDays input value
     */
    public void setIntervalDays(Integer intervalDays) { this.intervalDays = intervalDays; }
    /**
     * Retrieves next service date data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getNextServiceDate() { return nextServiceDate; }
    /**
     * Performs the setNextServiceDate operation in this module.
     *
     * @param nextServiceDate the nextServiceDate input value
     */
    public void setNextServiceDate(LocalDate nextServiceDate) { this.nextServiceDate = nextServiceDate; }
    /**
     * Retrieves trigger type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTriggerType() { return triggerType; }
    /**
     * Performs the setTriggerType operation in this module.
     *
     * @param triggerType the triggerType input value
     */
    public void setTriggerType(String triggerType) { this.triggerType = triggerType; }
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
}