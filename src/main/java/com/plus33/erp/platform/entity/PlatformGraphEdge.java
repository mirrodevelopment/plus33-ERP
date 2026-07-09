/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformGraphEdge.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformGraphEdgeController
 * Related Service   : PlatformGraphEdgeService, PlatformGraphEdgeServiceImpl
 * Related Repository: PlatformGraphEdgeRepository
 * Related Entity    : PlatformGraphEdge
 * Related DTO       : N/A
 * Related Mapper    : PlatformGraphEdgeMapper
 * Related DB Table  : platform_graph_edge
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformGraphEdgeRepository, PlatformGraphEdgeMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_graph_edge'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformGraphEdge}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_graph_edge'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_graph_edge}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_graph_edge")
public class PlatformGraphEdge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source_node", nullable = false)
    @NotNull
    private Long sourceNode;

    @Column(name = "target_node", nullable = false)
    @NotNull
    private Long targetNode;

    @Column(name = "relationship_type", nullable = false)
    @NotNull
    @Size(max = 100)
    private String relationshipType;

    @Column(nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal confidence = BigDecimal.valueOf(100.00);

    @Column(nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal weight = BigDecimal.valueOf(1.00);

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
     * Retrieves source node data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getSourceNode() { return sourceNode; }
    /**
     * Performs the setSourceNode operation in this module.
     *
     * @param sourceNode the sourceNode input value
     */
    public void setSourceNode(Long sourceNode) { this.sourceNode = sourceNode; }
    /**
     * Retrieves target node data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getTargetNode() { return targetNode; }
    /**
     * Performs the setTargetNode operation in this module.
     *
     * @param targetNode the targetNode input value
     */
    public void setTargetNode(Long targetNode) { this.targetNode = targetNode; }
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
     * Retrieves confidence data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getConfidence() { return confidence; }
    /**
     * Performs the setConfidence operation in this module.
     *
     * @param confidence the confidence input value
     */
    public void setConfidence(BigDecimal confidence) { this.confidence = confidence; }
    /**
     * Retrieves weight data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getWeight() { return weight; }
    /**
     * Performs the setWeight operation in this module.
     *
     * @param weight the weight input value
     */
    public void setWeight(BigDecimal weight) { this.weight = weight; }
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