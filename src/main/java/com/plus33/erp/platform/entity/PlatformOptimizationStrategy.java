/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformOptimizationStrategy.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformOptimizationStrategyController
 * Related Service   : PlatformOptimizationStrategyService, PlatformOptimizationStrategyServiceImpl
 * Related Repository: PlatformOptimizationStrategyRepository
 * Related Entity    : PlatformOptimizationStrategy
 * Related DTO       : N/A
 * Related Mapper    : PlatformOptimizationStrategyMapper
 * Related DB Table  : platform_optimization_strategy
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformOptimizationStrategyRepository, PlatformOptimizationStrategyMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_optimization_strategy'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformOptimizationStrategy}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_optimization_strategy'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_optimization_strategy}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_optimization_strategy")
public class PlatformOptimizationStrategy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "strategy_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String strategyCode;

    @Column(name = "strategy_name", nullable = false)
    @NotNull
    @Size(max = 150)
    private String strategyName;

    @Column(name = "parameters_json", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String parametersJson;

    @Column(nullable = false)
    @NotNull
    private Boolean active = true;

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
     * Retrieves strategy code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getStrategyCode() { return strategyCode; }
    /**
     * Performs the setStrategyCode operation in this module.
     *
     * @param strategyCode the strategyCode input value
     */
    public void setStrategyCode(String strategyCode) { this.strategyCode = strategyCode; }
    /**
     * Retrieves strategy name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getStrategyName() { return strategyName; }
    /**
     * Performs the setStrategyName operation in this module.
     *
     * @param strategyName the strategyName input value
     */
    public void setStrategyName(String strategyName) { this.strategyName = strategyName; }
    /**
     * Retrieves parameters json data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getParametersJson() { return parametersJson; }
    /**
     * Performs the setParametersJson operation in this module.
     *
     * @param parametersJson the parametersJson input value
     */
    public void setParametersJson(String parametersJson) { this.parametersJson = parametersJson; }
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