/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.entity
 * File              : BiKpiFormulaVersion.java
 * Purpose           : JPA Entity representing a persistent database record in Bi Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiKpiFormulaVersionController
 * Related Service   : BiKpiFormulaVersionService, BiKpiFormulaVersionServiceImpl
 * Related Repository: BiKpiFormulaVersionRepository
 * Related Entity    : BiKpiFormulaVersion
 * Related DTO       : N/A
 * Related Mapper    : BiKpiFormulaVersionMapper
 * Related DB Table  : bi_kpi_formula_version
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BiKpiFormulaVersionRepository, BiKpiFormulaVersionMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'bi_kpi_formula_version'. Defines persistent domain object for Bi Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code BiKpiFormulaVersion}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'bi_kpi_formula_version'.</p>
 *
 * <p><b>Database Table   :</b> {@code bi_kpi_formula_version}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "bi_kpi_formula_version")
public class BiKpiFormulaVersion {
    /**
     * Retrieves id data from the database.
     *
     * @param id the unique database ID of the resource
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "kpi_id", nullable = false) private Long kpiId;
    @Column(name = "version_number", nullable = false) private Integer versionNumber = 1;
    @Column(name = "formula_expression", nullable = false, columnDefinition = "TEXT") private String formulaExpression;
    @Column(name = "compiled_expression", columnDefinition = "TEXT") private String compiledExpression;
    @Column(name = "effective_from", nullable = false) private LocalDate effectiveFrom;
    @Column(name = "effective_to") private LocalDate effectiveTo;
    @Column(name = "is_current", nullable = false) private Boolean isCurrent = true;
    @Column(name = "published_by", length = 100) private String publishedBy;
    @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt = LocalDateTime.now();
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    /**
     * Retrieves kpi id data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getKpiId() { return kpiId; } public void setKpiId(Long v) { this.kpiId = v; }
    /**
     * Retrieves version number data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getVersionNumber() { return versionNumber; } public void setVersionNumber(Integer v) { this.versionNumber = v; }
    /**
     * Retrieves formula expression data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getFormulaExpression() { return formulaExpression; } public void setFormulaExpression(String v) { this.formulaExpression = v; }
    /**
     * Retrieves compiled expression data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCompiledExpression() { return compiledExpression; } public void setCompiledExpression(String v) { this.compiledExpression = v; }
    /**
     * Retrieves effective from data from the database.
     *
     * @param v the v input value
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getEffectiveFrom() { return effectiveFrom; } public void setEffectiveFrom(LocalDate v) { this.effectiveFrom = v; }
    /**
     * Retrieves effective to data from the database.
     *
     * @param v the v input value
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getEffectiveTo() { return effectiveTo; } public void setEffectiveTo(LocalDate v) { this.effectiveTo = v; }
    /**
     * Retrieves is current data from the database.
     *
     * @param v the v input value
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getIsCurrent() { return isCurrent; } public void setIsCurrent(Boolean v) { this.isCurrent = v; }
    /**
     * Retrieves published by data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPublishedBy() { return publishedBy; } public void setPublishedBy(String v) { this.publishedBy = v; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
}