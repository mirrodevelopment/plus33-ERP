/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformSloMeasurement.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformSloMeasurementController
 * Related Service   : PlatformSloMeasurementService, PlatformSloMeasurementServiceImpl
 * Related Repository: PlatformSloMeasurementRepository
 * Related Entity    : PlatformSloMeasurement
 * Related DTO       : N/A
 * Related Mapper    : PlatformSloMeasurementMapper
 * Related DB Table  : platform_slo_measurement
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformSloMeasurementRepository, PlatformSloMeasurementMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_slo_measurement'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformSloMeasurement}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_slo_measurement'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_slo_measurement}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_slo_measurement")
public class PlatformSloMeasurement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "slo_id", nullable = false)
    @NotNull
    private Long sloId;

    @Column(name = "current_percentage", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal currentPercentage;

    @Column(name = "error_budget", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal errorBudget;

    @Column(name = "recorded_at", nullable = false)
    @NotNull
    private LocalDateTime recordedAt = LocalDateTime.now();

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
     * Retrieves slo id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getSloId() { return sloId; }
    /**
     * Performs the setSloId operation in this module.
     *
     * @param sloId the sloId input value
     */
    public void setSloId(Long sloId) { this.sloId = sloId; }
    /**
     * Retrieves current percentage data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getCurrentPercentage() { return currentPercentage; }
    /**
     * Performs the setCurrentPercentage operation in this module.
     *
     * @param currentPercentage the currentPercentage input value
     */
    public void setCurrentPercentage(BigDecimal currentPercentage) { this.currentPercentage = currentPercentage; }
    /**
     * Retrieves error budget data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getErrorBudget() { return errorBudget; }
    /**
     * Performs the setErrorBudget operation in this module.
     *
     * @param errorBudget the errorBudget input value
     */
    public void setErrorBudget(BigDecimal errorBudget) { this.errorBudget = errorBudget; }
    /**
     * Retrieves recorded at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getRecordedAt() { return recordedAt; }
    /**
     * Performs the setRecordedAt operation in this module.
     *
     * @param recordedAt the recordedAt input value
     */
    public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }
}