/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformTwinRelation.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformTwinRelationController
 * Related Service   : PlatformTwinRelationService, PlatformTwinRelationServiceImpl
 * Related Repository: PlatformTwinRelationRepository
 * Related Entity    : PlatformTwinRelation
 * Related DTO       : N/A
 * Related Mapper    : PlatformTwinRelationMapper
 * Related DB Table  : platform_twin_relation
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformTwinRelationRepository, PlatformTwinRelationMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_twin_relation'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformTwinRelation}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_twin_relation'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_twin_relation}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_twin_relation")
public class PlatformTwinRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "source_instance_id", nullable = false)
    @NotNull
    private Long sourceInstanceId;

    @Column(name = "target_instance_id", nullable = false)
    @NotNull
    private Long targetInstanceId;

    @Column(name = "relationship_type", nullable = false)
    @NotNull
    @Size(max = 100)
    private String relationshipType;

    @Column(name = "updated_at", nullable = false)
    @NotNull
    private LocalDateTime updatedAt = LocalDateTime.now();

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
     * Retrieves source instance id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getSourceInstanceId() { return sourceInstanceId; }
    /**
     * Performs the setSourceInstanceId operation in this module.
     *
     * @param sourceInstanceId the sourceInstanceId input value
     */
    public void setSourceInstanceId(Long sourceInstanceId) { this.sourceInstanceId = sourceInstanceId; }
    /**
     * Retrieves target instance id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getTargetInstanceId() { return targetInstanceId; }
    /**
     * Performs the setTargetInstanceId operation in this module.
     *
     * @param targetInstanceId the targetInstanceId input value
     */
    public void setTargetInstanceId(Long targetInstanceId) { this.targetInstanceId = targetInstanceId; }
    /**
     * Retrieves relationship type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRelationshipType() { return relationshipType; }
    /**
     * Performs the setRelationshipType operation in this module.
     *
     * @param relationshipType the relationshipType input value
     */
    public void setRelationshipType(String relationshipType) { this.relationshipType = relationshipType; }
    /**
     * Retrieves updated at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    /**
     * Performs the setUpdatedAt operation in this module.
     *
     * @param updatedAt the updatedAt input value
     */
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}