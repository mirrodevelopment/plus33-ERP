/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.entity
 * File              : EmployeeSalaryStructureItem.java
 * Purpose           : JPA Entity representing a persistent database record in Workforce Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EmployeeSalaryStructureItemController
 * Related Service   : EmployeeSalaryStructureItemService, EmployeeSalaryStructureItemServiceImpl
 * Related Repository: EmployeeSalaryStructureItemRepository
 * Related Entity    : EmployeeSalaryStructureItem
 * Related DTO       : N/A
 * Related Mapper    : EmployeeSalaryStructureItemMapper
 * Related DB Table  : employee_salary_structure_items
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : EmployeeSalaryStructureItemRepository, EmployeeSalaryStructureItemMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'employee_salary_structure_items'. Defines persistent domain object for Workforce Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code EmployeeSalaryStructureItem}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'employee_salary_structure_items'.</p>
 *
 * <p><b>Database Table   :</b> {@code employee_salary_structure_items}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "employee_salary_structure_items")
public class EmployeeSalaryStructureItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "structure_id", nullable = false)
    private Long structureId;

    @Column(name = "component_id", nullable = false)
    private Long componentId;

    @Column(nullable = false, precision = 14, scale = 4)
    private BigDecimal amount = BigDecimal.ZERO;

    @Column(precision = 7, scale = 4)
    private BigDecimal percentage;

    @Column(name = "formula_expression")
    private String formulaExpression;

    @Column(nullable = false)
    private boolean active = true;

    public EmployeeSalaryStructureItem() {}

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
     * Retrieves structure id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getStructureId() { return structureId; }
    /**
     * Performs the setStructureId operation in this module.
     *
     * @param structureId the structureId input value
     */
    public void setStructureId(Long structureId) { this.structureId = structureId; }
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
     * Retrieves percentage data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getPercentage() { return percentage; }
    /**
     * Performs the setPercentage operation in this module.
     *
     * @param percentage the percentage input value
     */
    public void setPercentage(BigDecimal percentage) { this.percentage = percentage; }
    /**
     * Retrieves formula expression data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getFormulaExpression() { return formulaExpression; }
    /**
     * Performs the setFormulaExpression operation in this module.
     *
     * @param formulaExpression the formulaExpression input value
     */
    public void setFormulaExpression(String formulaExpression) { this.formulaExpression = formulaExpression; }
    /**
     * Performs the isActive operation in this module.
     *
     * @return true if operation succeeded, false otherwise
     */
    public boolean isActive() { return active; }
    /**
     * Performs the setActive operation in this module.
     *
     * @param active the active input value
     */
    public void setActive(boolean active) { this.active = active; }
}