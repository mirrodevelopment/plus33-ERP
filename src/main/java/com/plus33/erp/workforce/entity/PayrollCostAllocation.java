/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.entity
 * File              : PayrollCostAllocation.java
 * Purpose           : JPA Entity representing a persistent database record in Workforce Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PayrollCostAllocationController
 * Related Service   : PayrollCostAllocationService, PayrollCostAllocationServiceImpl
 * Related Repository: PayrollCostAllocationRepository
 * Related Entity    : PayrollCostAllocation
 * Related DTO       : N/A
 * Related Mapper    : PayrollCostAllocationMapper
 * Related DB Table  : payroll_cost_allocations
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PayrollCostAllocationRepository, PayrollCostAllocationMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'payroll_cost_allocations'. Defines persistent domain object for Workforce Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code PayrollCostAllocation}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'payroll_cost_allocations'.</p>
 *
 * <p><b>Database Table   :</b> {@code payroll_cost_allocations}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "payroll_cost_allocations")
public class PayrollCostAllocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payroll_run_item_id", nullable = false)
    private Long payrollRunItemId;

    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "cost_center_id")
    private Long costCenterId;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "allocation_percentage", nullable = false, precision = 5, scale = 2)
    private BigDecimal allocationPercentage = new BigDecimal("100.00");

    public PayrollCostAllocation() {}

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
     * Retrieves payroll run item id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getPayrollRunItemId() { return payrollRunItemId; }
    /**
     * Performs the setPayrollRunItemId operation in this module.
     *
     * @param payrollRunItemId the payrollRunItemId input value
     */
    public void setPayrollRunItemId(Long payrollRunItemId) { this.payrollRunItemId = payrollRunItemId; }
    /**
     * Retrieves department id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getDepartmentId() { return departmentId; }
    /**
     * Performs the setDepartmentId operation in this module.
     *
     * @param departmentId the departmentId input value
     */
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
    /**
     * Retrieves cost center id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCostCenterId() { return costCenterId; }
    /**
     * Performs the setCostCenterId operation in this module.
     *
     * @param costCenterId the costCenterId input value
     */
    public void setCostCenterId(Long costCenterId) { this.costCenterId = costCenterId; }
    /**
     * Retrieves project id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getProjectId() { return projectId; }
    /**
     * Performs the setProjectId operation in this module.
     *
     * @param projectId the projectId input value
     */
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    /**
     * Retrieves a paginated list of allocation percentage records.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getAllocationPercentage() { return allocationPercentage; }
    /**
     * Performs the setAllocationPercentage operation in this module.
     *
     * @param allocationPercentage the allocationPercentage input value
     */
    public void setAllocationPercentage(BigDecimal allocationPercentage) { this.allocationPercentage = allocationPercentage; }
}