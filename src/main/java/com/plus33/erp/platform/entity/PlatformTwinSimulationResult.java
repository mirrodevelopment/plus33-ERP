/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformTwinSimulationResult.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformTwinSimulationResultController
 * Related Service   : PlatformTwinSimulationResultService, PlatformTwinSimulationResultServiceImpl
 * Related Repository: PlatformTwinSimulationResultRepository
 * Related Entity    : PlatformTwinSimulationResult
 * Related DTO       : N/A
 * Related Mapper    : PlatformTwinSimulationResultMapper
 * Related DB Table  : platform_twin_simulation_result
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformTwinSimulationResultRepository, PlatformTwinSimulationResultMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_twin_simulation_result'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformTwinSimulationResult}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_twin_simulation_result'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_twin_simulation_result}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_twin_simulation_result")
public class PlatformTwinSimulationResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "scenario_id", nullable = false)
    @NotNull
    private Long scenarioId;

    @Column(name = "simulation_output", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String simulationOutput;

    @Column(nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal confidence;

    @Column(name = "simulated_at", nullable = false)
    @NotNull
    private LocalDateTime simulatedAt = LocalDateTime.now();

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
     * Retrieves scenario id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getScenarioId() { return scenarioId; }
    /**
     * Performs the setScenarioId operation in this module.
     *
     * @param scenarioId the scenarioId input value
     */
    public void setScenarioId(Long scenarioId) { this.scenarioId = scenarioId; }
    /**
     * Retrieves simulation output data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSimulationOutput() { return simulationOutput; }
    /**
     * Performs the setSimulationOutput operation in this module.
     *
     * @param simulationOutput the simulationOutput input value
     */
    public void setSimulationOutput(String simulationOutput) { this.simulationOutput = simulationOutput; }
    /**
     * Retrieves confidence data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getConfidence() { return confidence; }
    /**
     * Performs the setConfidence operation in this module.
     *
     * @param confidence the confidence input value
     */
    public void setConfidence(BigDecimal confidence) { this.confidence = confidence; }
    /**
     * Retrieves simulated at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getSimulatedAt() { return simulatedAt; }
    /**
     * Performs the setSimulatedAt operation in this module.
     *
     * @param simulatedAt the simulatedAt input value
     */
    public void setSimulatedAt(LocalDateTime simulatedAt) { this.simulatedAt = simulatedAt; }
}