/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.entity
 * File              : BiSchemaEvolutionHistory.java
 * Purpose           : JPA Entity representing a persistent database record in Bi Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiSchemaEvolutionHistoryController
 * Related Service   : BiSchemaEvolutionHistoryService, BiSchemaEvolutionHistoryServiceImpl
 * Related Repository: BiSchemaEvolutionHistoryRepository
 * Related Entity    : BiSchemaEvolutionHistory
 * Related DTO       : N/A
 * Related Mapper    : BiSchemaEvolutionHistoryMapper
 * Related DB Table  : bi_schema_evolution_history
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BiSchemaEvolutionHistoryRepository, BiSchemaEvolutionHistoryMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'bi_schema_evolution_history'. Defines persistent domain object for Bi Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code BiSchemaEvolutionHistory}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'bi_schema_evolution_history'.</p>
 *
 * <p><b>Database Table   :</b> {@code bi_schema_evolution_history}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "bi_schema_evolution_history")
public class BiSchemaEvolutionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "table_name", nullable = false)
    private String tableName;
    @Column(name = "detected_at", nullable = false)
    private LocalDateTime detectedAt = LocalDateTime.now();
    @Column(name = "action_type", nullable = false)
    private String actionType;
    @Column(name = "action_detail", nullable = false)
    private String actionDetail;
    @Column(name = "validation_status", nullable = false)
    private String validationStatus = "PENDING";

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
     * Retrieves detected at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getDetectedAt() { return detectedAt; }
    /**
     * Performs the setDetectedAt operation in this module.
     *
     * @param detectedAt the detectedAt input value
     */
    public void setDetectedAt(LocalDateTime detectedAt) { this.detectedAt = detectedAt; }
    /**
     * Retrieves action type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getActionType() { return actionType; }
    /**
     * Performs the setActionType operation in this module.
     *
     * @param actionType the actionType input value
     */
    public void setActionType(String actionType) { this.actionType = actionType; }
    /**
     * Retrieves action detail data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getActionDetail() { return actionDetail; }
    /**
     * Performs the setActionDetail operation in this module.
     *
     * @param actionDetail the actionDetail input value
     */
    public void setActionDetail(String actionDetail) { this.actionDetail = actionDetail; }
    /**
     * Retrieves validation status data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getValidationStatus() { return validationStatus; }
    /**
     * Performs the setValidationStatus operation in this module.
     *
     * @param validationStatus the validationStatus input value
     */
    public void setValidationStatus(String validationStatus) { this.validationStatus = validationStatus; }
}