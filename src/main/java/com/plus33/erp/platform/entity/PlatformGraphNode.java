/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformGraphNode.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformGraphNodeController
 * Related Service   : PlatformGraphNodeService, PlatformGraphNodeServiceImpl
 * Related Repository: PlatformGraphNodeRepository
 * Related Entity    : PlatformGraphNode
 * Related DTO       : N/A
 * Related Mapper    : PlatformGraphNodeMapper
 * Related DB Table  : platform_graph_node
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformGraphNodeRepository, PlatformGraphNodeMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_graph_node'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformGraphNode}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_graph_node'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_graph_node}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_graph_node")
public class PlatformGraphNode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "node_type", nullable = false)
    @NotNull
    @Size(max = 100)
    private String nodeType;

    @Column(name = "business_key", nullable = false, unique = true)
    @NotNull
    @Size(max = 150)
    private String businessKey;

    @Column(name = "display_name", nullable = false)
    @NotNull
    @Size(max = 200)
    private String displayName;

    @Column(nullable = false)
    @NotNull
    @Size(max = 100)
    private String module;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "ACTIVE";

    @Column(name = "metadata_json", columnDefinition = "TEXT")
    private String metadataJson;

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
     * Retrieves node type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getNodeType() { return nodeType; }
    /**
     * Performs the setNodeType operation in this module.
     *
     * @param nodeType the nodeType input value
     */
    public void setNodeType(String nodeType) { this.nodeType = nodeType; }
    /**
     * Retrieves business key data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getBusinessKey() { return businessKey; }
    /**
     * Performs the setBusinessKey operation in this module.
     *
     * @param businessKey the businessKey input value
     */
    public void setBusinessKey(String businessKey) { this.businessKey = businessKey; }
    /**
     * Retrieves display name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDisplayName() { return displayName; }
    /**
     * Performs the setDisplayName operation in this module.
     *
     * @param displayName the displayName input value
     */
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    /**
     * Retrieves module data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getModule() { return module; }
    /**
     * Performs the setModule operation in this module.
     *
     * @param module the module input value
     */
    public void setModule(String module) { this.module = module; }
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
     * Retrieves metadata json data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getMetadataJson() { return metadataJson; }
    /**
     * Performs the setMetadataJson operation in this module.
     *
     * @param metadataJson the metadataJson input value
     */
    public void setMetadataJson(String metadataJson) { this.metadataJson = metadataJson; }
}