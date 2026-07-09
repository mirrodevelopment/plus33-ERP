/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.entity
 * File              : RiskKri.java
 * Purpose           : JPA Entity representing a persistent database record in Grc Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: RiskKriController
 * Related Service   : RiskKriService, RiskKriServiceImpl
 * Related Repository: RiskKriRepository
 * Related Entity    : RiskKri
 * Related DTO       : N/A
 * Related Mapper    : RiskKriMapper
 * Related DB Table  : grc_risk_kris
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : RiskKriRepository, RiskKriMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'grc_risk_kris'. Defines persistent domain object for Grc Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.grc.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Grc Module</b>
 *
 * <p><b>Class  :</b> {@code RiskKri}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.grc.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'grc_risk_kris'.</p>
 *
 * <p><b>Database Table   :</b> {@code grc_risk_kris}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "grc_risk_kris")
public class RiskKri {
    /**
     * Retrieves id data from the database.
     *
     * @param id the unique database ID of the resource
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "risk_id", nullable = false) private Long riskId;
    @Column(name = "indicator_name", nullable = false, length = 150) private String indicatorName;
    @Column(name = "threshold_value", nullable = false) private BigDecimal thresholdValue;
    @Column(name = "current_value") private BigDecimal currentValue;
    @Column(nullable = false) private Boolean breached = false;
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    /**
     * Retrieves risk id data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getRiskId() { return riskId; } public void setRiskId(Long v) { this.riskId = v; }
    /**
     * Retrieves indicator name data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getIndicatorName() { return indicatorName; } public void setIndicatorName(String v) { this.indicatorName = v; }
    /**
     * Retrieves threshold value data from the database.
     *
     * @param v the v input value
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getThresholdValue() { return thresholdValue; } public void setThresholdValue(BigDecimal v) { this.thresholdValue = v; }
    /**
     * Retrieves current value data from the database.
     *
     * @param v the v input value
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getCurrentValue() { return currentValue; } public void setCurrentValue(BigDecimal v) { this.currentValue = v; }
    /**
     * Retrieves breached data from the database.
     *
     * @param v the v input value
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getBreached() { return breached; } public void setBreached(Boolean v) { this.breached = v; }
}