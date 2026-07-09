/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.entity
 * File              : GrcAnalyticsSnapshot.java
 * Purpose           : JPA Entity representing a persistent database record in Grc Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: GrcAnalyticsSnapshotController
 * Related Service   : GrcAnalyticsSnapshotService, GrcAnalyticsSnapshotServiceImpl
 * Related Repository: GrcAnalyticsSnapshotRepository
 * Related Entity    : GrcAnalyticsSnapshot
 * Related DTO       : N/A
 * Related Mapper    : GrcAnalyticsSnapshotMapper
 * Related DB Table  : grc_analytics_snapshots
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : GrcAnalyticsSnapshotRepository, GrcAnalyticsSnapshotMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'grc_analytics_snapshots'. Defines persistent domain object for Grc Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.grc.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <b>PLUS33 Coffee ERP -- Grc Module</b>
 *
 * <p><b>Class  :</b> {@code GrcAnalyticsSnapshot}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.grc.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'grc_analytics_snapshots'.</p>
 *
 * <p><b>Database Table   :</b> {@code grc_analytics_snapshots}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "grc_analytics_snapshots")
public class GrcAnalyticsSnapshot {
    /**
     * Retrieves id data from the database.
     *
     * @param id the unique database ID of the resource
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "company_id", nullable = false) private Long companyId;
    @Column(name = "metric_name", nullable = false, length = 80) private String metricName;
    @Column(name = "metric_value", nullable = false) private BigDecimal metricValue;
    @Column(name = "recorded_date", nullable = false) private LocalDate recordedDate;
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
     * Retrieves metric name data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getMetricName() { return metricName; } public void setMetricName(String v) { this.metricName = v; }
    /**
     * Retrieves metric value data from the database.
     *
     * @param v the v input value
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getMetricValue() { return metricValue; } public void setMetricValue(BigDecimal v) { this.metricValue = v; }
    /**
     * Retrieves recorded date data from the database.
     *
     * @param v the v input value
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getRecordedDate() { return recordedDate; } public void setRecordedDate(LocalDate v) { this.recordedDate = v; }
}