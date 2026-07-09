/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformTwinSimulationScenario.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformTwinSimulationScenarioController
 * Related Service   : PlatformTwinSimulationScenarioService, PlatformTwinSimulationScenarioServiceImpl
 * Related Repository: PlatformTwinSimulationScenarioRepository
 * Related Entity    : PlatformTwinSimulationScenario
 * Related DTO       : N/A
 * Related Mapper    : PlatformTwinSimulationScenarioMapper
 * Related DB Table  : platform_twin_simulation_scenario
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformTwinSimulationScenarioRepository, PlatformTwinSimulationScenarioMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_twin_simulation_scenario'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformTwinSimulationScenario}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_twin_simulation_scenario'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_twin_simulation_scenario}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_twin_simulation_scenario")
public class PlatformTwinSimulationScenario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "scenario_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String scenarioCode;

    @Column(name = "scenario_name", nullable = false)
    @NotNull
    @Size(max = 150)
    private String scenarioName;

    @Column(name = "config_variables", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String configVariables;

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
     * Retrieves scenario code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getScenarioCode() { return scenarioCode; }
    /**
     * Performs the setScenarioCode operation in this module.
     *
     * @param scenarioCode the scenarioCode input value
     */
    public void setScenarioCode(String scenarioCode) { this.scenarioCode = scenarioCode; }
    /**
     * Retrieves scenario name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getScenarioName() { return scenarioName; }
    /**
     * Performs the setScenarioName operation in this module.
     *
     * @param scenarioName the scenarioName input value
     */
    public void setScenarioName(String scenarioName) { this.scenarioName = scenarioName; }
    /**
     * Retrieves config variables data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getConfigVariables() { return configVariables; }
    /**
     * Performs the setConfigVariables operation in this module.
     *
     * @param configVariables the configVariables input value
     */
    public void setConfigVariables(String configVariables) { this.configVariables = configVariables; }
}