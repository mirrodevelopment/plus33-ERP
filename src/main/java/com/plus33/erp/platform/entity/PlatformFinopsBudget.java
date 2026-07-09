/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformFinopsBudget.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformFinopsBudgetController
 * Related Service   : PlatformFinopsBudgetService, PlatformFinopsBudgetServiceImpl
 * Related Repository: PlatformFinopsBudgetRepository
 * Related Entity    : PlatformFinopsBudget
 * Related DTO       : N/A
 * Related Mapper    : PlatformFinopsBudgetMapper
 * Related DB Table  : platform_finops_budget
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformFinopsBudgetRepository, PlatformFinopsBudgetMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_finops_budget'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformFinopsBudget}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_finops_budget'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_finops_budget}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_finops_budget")
public class PlatformFinopsBudget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "budget_name", nullable = false, unique = true)
    @NotNull
    @Size(max = 150)
    private String budgetName;

    @Column(name = "limit_amount", nullable = false, precision = 19, scale = 4)
    @NotNull
    private BigDecimal limitAmount;

    @Column(name = "alert_threshold", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal alertThreshold;

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
     * Retrieves version data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getVersion() { return version; }
    /**
     * Performs the setVersion operation in this module.
     *
     * @param version the version input value
     */
    public void setVersion(Integer version) { this.version = version; }
    /**
     * Retrieves budget name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getBudgetName() { return budgetName; }
    /**
     * Performs the setBudgetName operation in this module.
     *
     * @param budgetName the budgetName input value
     */
    public void setBudgetName(String budgetName) { this.budgetName = budgetName; }
    /**
     * Retrieves limit amount data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getLimitAmount() { return limitAmount; }
    /**
     * Performs the setLimitAmount operation in this module.
     *
     * @param limitAmount the limitAmount input value
     */
    public void setLimitAmount(BigDecimal limitAmount) { this.limitAmount = limitAmount; }
    /**
     * Retrieves alert threshold data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getAlertThreshold() { return alertThreshold; }
    /**
     * Performs the setAlertThreshold operation in this module.
     *
     * @param alertThreshold the alertThreshold input value
     */
    public void setAlertThreshold(BigDecimal alertThreshold) { this.alertThreshold = alertThreshold; }
}