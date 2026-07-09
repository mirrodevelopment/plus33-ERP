/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformFuelEfficiencyAdvisor.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformFuelEfficiencyAdvisorController
 * Related Service   : PlatformFuelEfficiencyAdvisorService, PlatformFuelEfficiencyAdvisorServiceImpl
 * Related Repository: PlatformFuelEfficiencyAdvisorRepository
 * Related Entity    : PlatformFuelEfficiencyAdvisor
 * Related DTO       : N/A
 * Related Mapper    : PlatformFuelEfficiencyAdvisorMapper
 * Related DB Table  : platform_fuel_efficiency_advisor
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformFuelEfficiencyAdvisorRepository, PlatformFuelEfficiencyAdvisorMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_fuel_efficiency_advisor'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
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
 * <p><b>Class  :</b> {@code PlatformFuelEfficiencyAdvisor}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_fuel_efficiency_advisor'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_fuel_efficiency_advisor}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_fuel_efficiency_advisor")
public class PlatformFuelEfficiencyAdvisor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recommendation_type", nullable = false)
    @NotNull
    @Size(max = 100)
    private String recommendationType; // EcoSpeedLimit, ReduceIdle, CruiseControl

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String priority;

    @Column(name = "expected_fuel_saving_l", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal expectedFuelSavingL;

    @Column(name = "expected_cost_saving", nullable = false, precision = 12, scale = 2)
    @NotNull
    private BigDecimal expectedCostSaving;

    @Column(name = "expected_emission_reduce", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal expectedEmissionReduce;

    @Column(name = "generated_by", nullable = false)
    @NotNull
    @Size(max = 100)
    private String generatedBy;

    @Column(nullable = false)
    @NotNull
    private Boolean acknowledged = false;

    @Column(name = "applied_at")
    private LocalDateTime appliedAt;

    @Column(name = "created_at", nullable = false)
    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

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
     * Retrieves recommendation type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRecommendationType() { return recommendationType; }
    /**
     * Performs the setRecommendationType operation in this module.
     *
     * @param recommendationType the recommendationType input value
     */
    public void setRecommendationType(String recommendationType) { this.recommendationType = recommendationType; }
    /**
     * Retrieves priority data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPriority() { return priority; }
    /**
     * Performs the setPriority operation in this module.
     *
     * @param priority the priority input value
     */
    public void setPriority(String priority) { this.priority = priority; }
    /**
     * Retrieves expected fuel saving l data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getExpectedFuelSavingL() { return expectedFuelSavingL; }
    /**
     * Performs the setExpectedFuelSavingL operation in this module.
     *
     * @param expectedFuelSavingL the expectedFuelSavingL input value
     */
    public void setExpectedFuelSavingL(BigDecimal expectedFuelSavingL) { this.expectedFuelSavingL = expectedFuelSavingL; }
    /**
     * Retrieves expected cost saving data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getExpectedCostSaving() { return expectedCostSaving; }
    /**
     * Performs the setExpectedCostSaving operation in this module.
     *
     * @param expectedCostSaving the expectedCostSaving input value
     */
    public void setExpectedCostSaving(BigDecimal expectedCostSaving) { this.expectedCostSaving = expectedCostSaving; }
    /**
     * Retrieves expected emission reduce data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getExpectedEmissionReduce() { return expectedEmissionReduce; }
    /**
     * Performs the setExpectedEmissionReduce operation in this module.
     *
     * @param expectedEmissionReduce the expectedEmissionReduce input value
     */
    public void setExpectedEmissionReduce(BigDecimal expectedEmissionReduce) { this.expectedEmissionReduce = expectedEmissionReduce; }
    /**
     * Retrieves generated by data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getGeneratedBy() { return generatedBy; }
    /**
     * Performs the setGeneratedBy operation in this module.
     *
     * @param generatedBy the generatedBy input value
     */
    public void setGeneratedBy(String generatedBy) { this.generatedBy = generatedBy; }
    /**
     * Retrieves acknowledged data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getAcknowledged() { return acknowledged; }
    /**
     * Performs the setAcknowledged operation in this module.
     *
     * @param acknowledged the acknowledged input value
     */
    public void setAcknowledged(Boolean acknowledged) { this.acknowledged = acknowledged; }
    /**
     * Retrieves applied at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getAppliedAt() { return appliedAt; }
    /**
     * Performs the setAppliedAt operation in this module.
     *
     * @param appliedAt the appliedAt input value
     */
    public void setAppliedAt(LocalDateTime appliedAt) { this.appliedAt = appliedAt; }
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
}