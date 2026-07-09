/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.entity
 * File              : BiAnalyticsSnapshot.java
 * Purpose           : JPA Entity representing a persistent database record in Bi Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiAnalyticsSnapshotController
 * Related Service   : BiAnalyticsSnapshotService, BiAnalyticsSnapshotServiceImpl
 * Related Repository: BiAnalyticsSnapshotRepository
 * Related Entity    : BiAnalyticsSnapshot
 * Related DTO       : N/A
 * Related Mapper    : BiAnalyticsSnapshotMapper
 * Related DB Table  : bi_analytics_snapshot
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BiAnalyticsSnapshotRepository, BiAnalyticsSnapshotMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'bi_analytics_snapshot'. Defines persistent domain object for Bi Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code BiAnalyticsSnapshot}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'bi_analytics_snapshot'.</p>
 *
 * <p><b>Database Table   :</b> {@code bi_analytics_snapshot}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "bi_analytics_snapshot")
public class BiAnalyticsSnapshot {
    /**
     * Retrieves id data from the database.
     *
     * @param id the unique database ID of the resource
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "company_id", nullable = false) private Long companyId;
    @Column(name = "snapshot_date", nullable = false) private LocalDate snapshotDate;
    @Column(name = "snapshot_period", nullable = false, length = 20) private String snapshotPeriod = "MONTHLY";
    @Column(name = "kpi_code", nullable = false, length = 100) private String kpiCode;
    @Column(name = "kpi_value", precision = 19, scale = 4) private BigDecimal kpiValue;
    @Column(name = "kpi_unit", length = 50) private String kpiUnit;
    @Column(name = "dimension_filters", columnDefinition = "TEXT") private String dimensionFilters;
    @Column(name = "source_job_run_id") private Long sourceJobRunId;
    @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt = LocalDateTime.now();
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    /**
     * Retrieves company id data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCompanyId() { return companyId; } public void setCompanyId(Long v) { this.companyId = v; }
    /**
     * Retrieves snapshot date data from the database.
     *
     * @param v the v input value
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getSnapshotDate() { return snapshotDate; } public void setSnapshotDate(LocalDate v) { this.snapshotDate = v; }
    /**
     * Retrieves snapshot period data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSnapshotPeriod() { return snapshotPeriod; } public void setSnapshotPeriod(String v) { this.snapshotPeriod = v; }
    /**
     * Retrieves kpi code data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getKpiCode() { return kpiCode; } public void setKpiCode(String v) { this.kpiCode = v; }
    /**
     * Retrieves kpi value data from the database.
     *
     * @param v the v input value
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getKpiValue() { return kpiValue; } public void setKpiValue(BigDecimal v) { this.kpiValue = v; }
    /**
     * Retrieves kpi unit data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getKpiUnit() { return kpiUnit; } public void setKpiUnit(String v) { this.kpiUnit = v; }
    /**
     * Retrieves dimension filters data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDimensionFilters() { return dimensionFilters; } public void setDimensionFilters(String v) { this.dimensionFilters = v; }
    /**
     * Retrieves source job run id data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getSourceJobRunId() { return sourceJobRunId; } public void setSourceJobRunId(Long v) { this.sourceJobRunId = v; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
}