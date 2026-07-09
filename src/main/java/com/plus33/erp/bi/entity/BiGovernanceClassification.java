/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.entity
 * File              : BiGovernanceClassification.java
 * Purpose           : JPA Entity representing a persistent database record in Bi Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiGovernanceClassificationController
 * Related Service   : BiGovernanceClassificationService, BiGovernanceClassificationServiceImpl
 * Related Repository: BiGovernanceClassificationRepository
 * Related Entity    : BiGovernanceClassification
 * Related DTO       : N/A
 * Related Mapper    : BiGovernanceClassificationMapper
 * Related DB Table  : bi_governance_classification
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BiGovernanceClassificationRepository, BiGovernanceClassificationMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'bi_governance_classification'. Defines persistent domain object for Bi Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code BiGovernanceClassification}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'bi_governance_classification'.</p>
 *
 * <p><b>Database Table   :</b> {@code bi_governance_classification}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "bi_governance_classification")
public class BiGovernanceClassification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "table_name", nullable = false)
    private String tableName;
    @Column(name = "column_name", nullable = false)
    private String columnName;
    @Column(name = "classification_level", nullable = false)
    private String classificationLevel;
    private String description;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

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
     * Retrieves table name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTableName() { return tableName; }
    /**
     * Performs the setTableName operation in this module.
     *
     * @param tableName the tableName input value
     */
    public void setTableName(String tableName) { this.tableName = tableName; }
    /**
     * Retrieves column name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getColumnName() { return columnName; }
    /**
     * Performs the setColumnName operation in this module.
     *
     * @param columnName the columnName input value
     */
    public void setColumnName(String columnName) { this.columnName = columnName; }
    /**
     * Retrieves classification level data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getClassificationLevel() { return classificationLevel; }
    /**
     * Performs the setClassificationLevel operation in this module.
     *
     * @param classificationLevel the classificationLevel input value
     */
    public void setClassificationLevel(String classificationLevel) { this.classificationLevel = classificationLevel; }
    /**
     * Retrieves description data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDescription() { return description; }
    /**
     * Performs the setDescription operation in this module.
     *
     * @param description the description input value
     */
    public void setDescription(String description) { this.description = description; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
    /**
     * Performs the setCreatedAt operation in this module.
     *
     * @param createdAt the createdAt input value
     */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}