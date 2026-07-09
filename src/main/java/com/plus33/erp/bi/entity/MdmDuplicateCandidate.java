/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.entity
 * File              : MdmDuplicateCandidate.java
 * Purpose           : JPA Entity representing a persistent database record in Bi Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: MdmDuplicateCandidateController
 * Related Service   : MdmDuplicateCandidateService, MdmDuplicateCandidateServiceImpl
 * Related Repository: MdmDuplicateCandidateRepository
 * Related Entity    : MdmDuplicateCandidate
 * Related DTO       : N/A
 * Related Mapper    : MdmDuplicateCandidateMapper
 * Related DB Table  : bi_mdm_duplicate_candidate
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : MdmDuplicateCandidateRepository, MdmDuplicateCandidateMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'bi_mdm_duplicate_candidate'. Defines persistent domain object for Bi Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code MdmDuplicateCandidate}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'bi_mdm_duplicate_candidate'.</p>
 *
 * <p><b>Database Table   :</b> {@code bi_mdm_duplicate_candidate}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "bi_mdm_duplicate_candidate")
public class MdmDuplicateCandidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "record_type", nullable = false)
    @NotNull
    @Size(max = 50)
    private String recordType;

    @Column(name = "source_system_a", nullable = false)
    @NotNull
    @Size(max = 100)
    private String sourceSystemA;

    @Column(name = "source_table_a", nullable = false)
    @NotNull
    @Size(max = 100)
    private String sourceTableA;

    @Column(name = "source_dim_id_a", nullable = false)
    @NotNull
    private Long sourceDimIdA;

    @Column(name = "source_system_b", nullable = false)
    @NotNull
    @Size(max = 100)
    private String sourceSystemB;

    @Column(name = "source_table_b", nullable = false)
    @NotNull
    @Size(max = 100)
    private String sourceTableB;

    @Column(name = "source_dim_id_b", nullable = false)
    @NotNull
    private Long sourceDimIdB;

    @Column(name = "similarity_score", nullable = false)
    @NotNull
    private java.math.BigDecimal similarityScore;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "PENDING";

    @Column(name = "detected_at", nullable = false)
    @NotNull
    private LocalDateTime detectedAt = LocalDateTime.now();

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

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
     * Retrieves record type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRecordType() { return recordType; }
    /**
     * Performs the setRecordType operation in this module.
     *
     * @param recordType the recordType input value
     */
    public void setRecordType(String recordType) { this.recordType = recordType; }
    /**
     * Retrieves source system a data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSourceSystemA() { return sourceSystemA; }
    /**
     * Performs the setSourceSystemA operation in this module.
     *
     * @param sourceSystemA the sourceSystemA input value
     */
    public void setSourceSystemA(String sourceSystemA) { this.sourceSystemA = sourceSystemA; }
    /**
     * Retrieves source table a data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSourceTableA() { return sourceTableA; }
    /**
     * Performs the setSourceTableA operation in this module.
     *
     * @param sourceTableA the sourceTableA input value
     */
    public void setSourceTableA(String sourceTableA) { this.sourceTableA = sourceTableA; }
    /**
     * Retrieves source dim id a data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getSourceDimIdA() { return sourceDimIdA; }
    /**
     * Performs the setSourceDimIdA operation in this module.
     *
     * @param sourceDimIdA the sourceDimIdA input value
     */
    public void setSourceDimIdA(Long sourceDimIdA) { this.sourceDimIdA = sourceDimIdA; }
    /**
     * Retrieves source system b data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSourceSystemB() { return sourceSystemB; }
    /**
     * Performs the setSourceSystemB operation in this module.
     *
     * @param sourceSystemB the sourceSystemB input value
     */
    public void setSourceSystemB(String sourceSystemB) { this.sourceSystemB = sourceSystemB; }
    /**
     * Retrieves source table b data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSourceTableB() { return sourceTableB; }
    /**
     * Performs the setSourceTableB operation in this module.
     *
     * @param sourceTableB the sourceTableB input value
     */
    public void setSourceTableB(String sourceTableB) { this.sourceTableB = sourceTableB; }
    /**
     * Retrieves source dim id b data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getSourceDimIdB() { return sourceDimIdB; }
    /**
     * Performs the setSourceDimIdB operation in this module.
     *
     * @param sourceDimIdB the sourceDimIdB input value
     */
    public void setSourceDimIdB(Long sourceDimIdB) { this.sourceDimIdB = sourceDimIdB; }
    /**
     * Retrieves similarity score data from the database.
     *
     * @throws ResourceNotFoundException if the entity is not found
     */
    public java.math.BigDecimal getSimilarityScore() { return similarityScore; }
    /**
     * Performs the setSimilarityScore operation in this module.
     *
     * @param similarityScore the similarityScore input value
     */
    public void setSimilarityScore(java.math.BigDecimal similarityScore) { this.similarityScore = similarityScore; }
    /**
     * Retrieves status data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getStatus() { return status; }
    /**
     * Performs the setStatus operation in this module.
     *
     * @param status status filter for narrowing query results
     */
    public void setStatus(String status) { this.status = status; }
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
     * Retrieves resolved at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getResolvedAt() { return resolvedAt; }
    /**
     * Performs the setResolvedAt operation in this module.
     *
     * @param resolvedAt the resolvedAt input value
     */
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }
}