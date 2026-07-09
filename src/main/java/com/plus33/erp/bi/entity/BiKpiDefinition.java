/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.entity
 * File              : BiKpiDefinition.java
 * Purpose           : JPA Entity representing a persistent database record in Bi Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiKpiDefinitionController
 * Related Service   : BiKpiDefinitionService, BiKpiDefinitionServiceImpl
 * Related Repository: BiKpiDefinitionRepository
 * Related Entity    : BiKpiDefinition
 * Related DTO       : N/A
 * Related Mapper    : BiKpiDefinitionMapper
 * Related DB Table  : bi_kpi_definition
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BiKpiDefinitionRepository, BiKpiDefinitionMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'bi_kpi_definition'. Defines persistent domain object for Bi Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code BiKpiDefinition}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'bi_kpi_definition'.</p>
 *
 * <p><b>Database Table   :</b> {@code bi_kpi_definition}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "bi_kpi_definition")
public class BiKpiDefinition {
    /**
     * Retrieves id data from the database.
     *
     * @param id the unique database ID of the resource
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "kpi_code", nullable = false, unique = true, length = 100) private String kpiCode;
    @Column(name = "kpi_name", nullable = false, length = 200) private String kpiName;
    @Column(name = "kpi_category", length = 100) private String kpiCategory;
    @Column(columnDefinition = "TEXT") private String description;
    @Column(length = 50) private String unit;
    @Column(nullable = false, length = 10) private String direction = "HIGHER";
    @Column(name = "target_value", precision = 19, scale = 4) private BigDecimal targetValue;
    @Column(name = "threshold_warning", precision = 19, scale = 4) private BigDecimal thresholdWarning;
    @Column(name = "threshold_critical", precision = 19, scale = 4) private BigDecimal thresholdCritical;
    @Column(nullable = false, length = 30) private String status = "DRAFT";
    @Column(length = 100) private String owner;
    @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt = LocalDateTime.now();
    @Column(name = "updated_at", nullable = false) private LocalDateTime updatedAt = LocalDateTime.now();
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    /**
     * Retrieves kpi code data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getKpiCode() { return kpiCode; } public void setKpiCode(String v) { this.kpiCode = v; }
    /**
     * Retrieves kpi name data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getKpiName() { return kpiName; } public void setKpiName(String v) { this.kpiName = v; }
    /**
     * Retrieves kpi category data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getKpiCategory() { return kpiCategory; } public void setKpiCategory(String v) { this.kpiCategory = v; }
    /**
     * Retrieves description data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDescription() { return description; } public void setDescription(String v) { this.description = v; }
    /**
     * Retrieves unit data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getUnit() { return unit; } public void setUnit(String v) { this.unit = v; }
    /**
     * Retrieves direction data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDirection() { return direction; } public void setDirection(String v) { this.direction = v; }
    /**
     * Retrieves target value data from the database.
     *
     * @param v the v input value
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getTargetValue() { return targetValue; } public void setTargetValue(BigDecimal v) { this.targetValue = v; }
    /**
     * Retrieves threshold warning data from the database.
     *
     * @param v the v input value
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getThresholdWarning() { return thresholdWarning; } public void setThresholdWarning(BigDecimal v) { this.thresholdWarning = v; }
    /**
     * Retrieves threshold critical data from the database.
     *
     * @param v the v input value
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getThresholdCritical() { return thresholdCritical; } public void setThresholdCritical(BigDecimal v) { this.thresholdCritical = v; }
    /**
     * Retrieves status data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getStatus() { return status; } public void setStatus(String v) { this.status = v; }
    /**
     * Retrieves owner data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getOwner() { return owner; } public void setOwner(String v) { this.owner = v; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
    /**
     * Retrieves updated at data from the database.
     *
     * @param v the v input value
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getUpdatedAt() { return updatedAt; } public void setUpdatedAt(LocalDateTime v) { this.updatedAt = v; }
}