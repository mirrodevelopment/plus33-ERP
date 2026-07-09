/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.entity
 * File              : SalaryComponent.java
 * Purpose           : JPA Entity representing a persistent database record in Workforce Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SalaryComponentController
 * Related Service   : SalaryComponentService, SalaryComponentServiceImpl
 * Related Repository: SalaryComponentRepository
 * Related Entity    : SalaryComponent
 * Related DTO       : N/A
 * Related Mapper    : SalaryComponentMapper
 * Related DB Table  : salary_components
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : SalaryComponentRepository, SalaryComponentMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'salary_components'. Defines persistent domain object for Workforce Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code SalaryComponent}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'salary_components'.</p>
 *
 * <p><b>Database Table   :</b> {@code salary_components}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "salary_components")
public class SalaryComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(nullable = false, length = 50)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "component_type", nullable = false)
    private SalaryComponentType componentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "calculation_method", nullable = false)
    private CalculationMethod calculationMethod;

    @Column(name = "gl_account_id")
    private Long glAccountId;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    public SalaryComponent() {}

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
     * Retrieves component type data from the database.
     *
     * @return the SalaryComponentType result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public SalaryComponentType getComponentType() { return componentType; }
    /**
     * Performs the setComponentType operation in this module.
     *
     * @param componentType the componentType input value
     */
    public void setComponentType(SalaryComponentType componentType) { this.componentType = componentType; }
    /**
     * Retrieves calculation method data from the database.
     *
     * @return the CalculationMethod result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public CalculationMethod getCalculationMethod() { return calculationMethod; }
    /**
     * Performs the setCalculationMethod operation in this module.
     *
     * @param calculationMethod the calculationMethod input value
     */
    public void setCalculationMethod(CalculationMethod calculationMethod) { this.calculationMethod = calculationMethod; }
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
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
    /**
     * Performs the setCreatedAt operation in this module.
     *
     * @param createdAt the createdAt input value
     */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    /**
     * Retrieves updated at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    /**
     * Performs the setUpdatedAt operation in this module.
     *
     * @param updatedAt the updatedAt input value
     */
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}