/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.entity
 * File              : PayrollItemBreakdown.java
 * Purpose           : JPA Entity representing a persistent database record in Workforce Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PayrollItemBreakdownController
 * Related Service   : PayrollItemBreakdownService, PayrollItemBreakdownServiceImpl
 * Related Repository: PayrollItemBreakdownRepository
 * Related Entity    : PayrollItemBreakdown
 * Related DTO       : N/A
 * Related Mapper    : PayrollItemBreakdownMapper
 * Related DB Table  : payroll_item_breakdowns
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PayrollItemBreakdownRepository, PayrollItemBreakdownMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'payroll_item_breakdowns'. Defines persistent domain object for Workforce Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code PayrollItemBreakdown}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'payroll_item_breakdowns'.</p>
 *
 * <p><b>Database Table   :</b> {@code payroll_item_breakdowns}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "payroll_item_breakdowns")
public class PayrollItemBreakdown {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payroll_run_item_id", nullable = false)
    private Long payrollRunItemId;

    @Column(name = "component_id", nullable = false)
    private Long componentId;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal amount = BigDecimal.ZERO;

    private String description;

    public PayrollItemBreakdown() {}

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
     * Retrieves component id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getComponentId() { return componentId; }
    /**
     * Performs the setComponentId operation in this module.
     *
     * @param componentId the componentId input value
     */
    public void setComponentId(Long componentId) { this.componentId = componentId; }
    /**
     * Retrieves amount data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getAmount() { return amount; }
    /**
     * Performs the setAmount operation in this module.
     *
     * @param amount the amount input value
     */
    public void setAmount(BigDecimal amount) { this.amount = amount; }
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
}