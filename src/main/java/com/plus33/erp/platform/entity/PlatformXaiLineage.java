/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformXaiLineage.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformXaiLineageController
 * Related Service   : PlatformXaiLineageService, PlatformXaiLineageServiceImpl
 * Related Repository: PlatformXaiLineageRepository
 * Related Entity    : PlatformXaiLineage
 * Related DTO       : N/A
 * Related Mapper    : PlatformXaiLineageMapper
 * Related DB Table  : platform_xai_lineage
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformXaiLineageRepository, PlatformXaiLineageMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_xai_lineage'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformXaiLineage}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_xai_lineage'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_xai_lineage}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_xai_lineage")
public class PlatformXaiLineage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "decision_key", nullable = false, unique = true)
    @NotNull
    @Size(max = 150)
    private String decisionKey;

    @Column(name = "contributing_factors", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String contributingFactors;

    @Column(name = "model_version", nullable = false)
    @NotNull
    @Size(max = 50)
    private String modelVersion;

    @Column(name = "created_at", nullable = false)
    @NotNull
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
     * Retrieves version data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getVersion() { return version; }
    /**
     * Performs the setVersion operation in this module.
     *
     * @param version the version input value
     */
    public void setVersion(Integer version) { this.version = version; }
    /**
     * Retrieves decision key data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDecisionKey() { return decisionKey; }
    /**
     * Performs the setDecisionKey operation in this module.
     *
     * @param decisionKey the decisionKey input value
     */
    public void setDecisionKey(String decisionKey) { this.decisionKey = decisionKey; }
    /**
     * Retrieves contributing factors data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getContributingFactors() { return contributingFactors; }
    /**
     * Performs the setContributingFactors operation in this module.
     *
     * @param contributingFactors the contributingFactors input value
     */
    public void setContributingFactors(String contributingFactors) { this.contributingFactors = contributingFactors; }
    /**
     * Retrieves model version data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getModelVersion() { return modelVersion; }
    /**
     * Performs the setModelVersion operation in this module.
     *
     * @param modelVersion the modelVersion input value
     */
    public void setModelVersion(String modelVersion) { this.modelVersion = modelVersion; }
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