/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.entity
 * File              : MdmSourceMapping.java
 * Purpose           : JPA Entity representing a persistent database record in Bi Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: MdmSourceMappingController
 * Related Service   : MdmSourceMappingService, MdmSourceMappingServiceImpl
 * Related Repository: MdmSourceMappingRepository
 * Related Entity    : MdmSourceMapping
 * Related DTO       : N/A
 * Related Mapper    : MdmSourceMappingMapper
 * Related DB Table  : bi_mdm_source_mapping
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : MdmSourceMappingRepository, MdmSourceMappingMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'bi_mdm_source_mapping'. Defines persistent domain object for Bi Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code MdmSourceMapping}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'bi_mdm_source_mapping'.</p>
 *
 * <p><b>Database Table   :</b> {@code bi_mdm_source_mapping}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "bi_mdm_source_mapping")
public class MdmSourceMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "golden_record_id", nullable = false)
    @NotNull
    private MdmGoldenRecord goldenRecord;

    @Column(name = "source_system", nullable = false)
    @NotNull
    @Size(max = 100)
    private String sourceSystem;

    @Column(name = "source_table", nullable = false)
    @NotNull
    @Size(max = 100)
    private String sourceTable;

    @Column(name = "source_dim_id", nullable = false)
    @NotNull
    private Long sourceDimId;

    @Column(name = "confidence_score", nullable = false)
    @NotNull
    private java.math.BigDecimal confidenceScore = java.math.BigDecimal.valueOf(100);

    @Column(name = "mapped_at", nullable = false)
    @NotNull
    private LocalDateTime mappedAt = LocalDateTime.now();

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
     * Retrieves golden record data from the database.
     *
     * @return the MdmGoldenRecord result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public MdmGoldenRecord getGoldenRecord() { return goldenRecord; }
    /**
     * Performs the setGoldenRecord operation in this module.
     *
     * @param goldenRecord the goldenRecord input value
     */
    public void setGoldenRecord(MdmGoldenRecord goldenRecord) { this.goldenRecord = goldenRecord; }
    /**
     * Retrieves source system data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSourceSystem() { return sourceSystem; }
    /**
     * Performs the setSourceSystem operation in this module.
     *
     * @param sourceSystem the sourceSystem input value
     */
    public void setSourceSystem(String sourceSystem) { this.sourceSystem = sourceSystem; }
    /**
     * Retrieves source table data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSourceTable() { return sourceTable; }
    /**
     * Performs the setSourceTable operation in this module.
     *
     * @param sourceTable the sourceTable input value
     */
    public void setSourceTable(String sourceTable) { this.sourceTable = sourceTable; }
    /**
     * Retrieves source dim id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getSourceDimId() { return sourceDimId; }
    /**
     * Performs the setSourceDimId operation in this module.
     *
     * @param sourceDimId the sourceDimId input value
     */
    public void setSourceDimId(Long sourceDimId) { this.sourceDimId = sourceDimId; }
    /**
     * Retrieves confidence score data from the database.
     *
     * @throws ResourceNotFoundException if the entity is not found
     */
    public java.math.BigDecimal getConfidenceScore() { return confidenceScore; }
    /**
     * Performs the setConfidenceScore operation in this module.
     *
     * @param confidenceScore the confidenceScore input value
     */
    public void setConfidenceScore(java.math.BigDecimal confidenceScore) { this.confidenceScore = confidenceScore; }
    /**
     * Retrieves mapped at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getMappedAt() { return mappedAt; }
    /**
     * Performs the setMappedAt operation in this module.
     *
     * @param mappedAt the mappedAt input value
     */
    public void setMappedAt(LocalDateTime mappedAt) { this.mappedAt = mappedAt; }
}