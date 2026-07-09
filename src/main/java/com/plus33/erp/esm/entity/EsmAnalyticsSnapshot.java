/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Esm Module
 * Package           : com.plus33.erp.esm.entity
 * File              : EsmAnalyticsSnapshot.java
 * Purpose           : JPA Entity representing a persistent database record in Esm Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EsmAnalyticsSnapshotController
 * Related Service   : EsmAnalyticsSnapshotService, EsmAnalyticsSnapshotServiceImpl
 * Related Repository: EsmAnalyticsSnapshotRepository
 * Related Entity    : EsmAnalyticsSnapshot
 * Related DTO       : N/A
 * Related Mapper    : EsmAnalyticsSnapshotMapper
 * Related DB Table  : esm_analytics_snapshots
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : EsmAnalyticsSnapshotRepository, EsmAnalyticsSnapshotMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'esm_analytics_snapshots'. Defines persistent domain object for Esm Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.esm.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <b>PLUS33 Coffee ERP -- Esm Module</b>
 *
 * <p><b>Class  :</b> {@code EsmAnalyticsSnapshot}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.esm.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'esm_analytics_snapshots'.</p>
 *
 * <p><b>Database Table   :</b> {@code esm_analytics_snapshots}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "esm_analytics_snapshots")
public class EsmAnalyticsSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "metric_name", nullable = false, length = 50)
    private String metricName;

    @Column(name = "metric_value", nullable = false)
    private BigDecimal metricValue;

    @Column(name = "recorded_date", nullable = false)
    private LocalDate recordedDate;

    // Getters and setters
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
     * Retrieves metric name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getMetricName() { return metricName; }
    /**
     * Performs the setMetricName operation in this module.
     *
     * @param metricName the metricName input value
     */
    public void setMetricName(String metricName) { this.metricName = metricName; }
    /**
     * Retrieves metric value data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getMetricValue() { return metricValue; }
    /**
     * Performs the setMetricValue operation in this module.
     *
     * @param metricValue the metricValue input value
     */
    public void setMetricValue(BigDecimal metricValue) { this.metricValue = metricValue; }
    /**
     * Retrieves recorded date data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getRecordedDate() { return recordedDate; }
    /**
     * Performs the setRecordedDate operation in this module.
     *
     * @param recordedDate the recordedDate input value
     */
    public void setRecordedDate(LocalDate recordedDate) { this.recordedDate = recordedDate; }
}