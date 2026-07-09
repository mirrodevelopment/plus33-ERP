/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformLogisticsNode.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformLogisticsNodeController
 * Related Service   : PlatformLogisticsNodeService, PlatformLogisticsNodeServiceImpl
 * Related Repository: PlatformLogisticsNodeRepository
 * Related Entity    : PlatformLogisticsNode
 * Related DTO       : N/A
 * Related Mapper    : PlatformLogisticsNodeMapper
 * Related DB Table  : platform_logistics_node
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformLogisticsNodeRepository, PlatformLogisticsNodeMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_logistics_node'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformLogisticsNode}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_logistics_node'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_logistics_node}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_logistics_node")
public class PlatformLogisticsNode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "node_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String nodeCode;

    @Column(name = "node_type", nullable = false)
    @NotNull
    @Size(max = 100)
    private String nodeType;

    @Column(nullable = false, precision = 9, scale = 6)
    @NotNull
    private BigDecimal latitude;

    @Column(nullable = false, precision = 9, scale = 6)
    @NotNull
    private BigDecimal longitude;

    @Size(max = 50)
    private String geohash;

    @Column(nullable = false)
    @NotNull
    @Size(max = 100)
    private String region;

    @Column(nullable = false)
    @NotNull
    @Size(max = 100)
    private String timezone;

    @Column(nullable = false)
    @NotNull
    private Integer capacity = 0;

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
     * Retrieves node code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getNodeCode() { return nodeCode; }
    /**
     * Performs the setNodeCode operation in this module.
     *
     * @param nodeCode the nodeCode input value
     */
    public void setNodeCode(String nodeCode) { this.nodeCode = nodeCode; }
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
     * Retrieves latitude data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getLatitude() { return latitude; }
    /**
     * Performs the setLatitude operation in this module.
     *
     * @param latitude the latitude input value
     */
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }
    /**
     * Retrieves longitude data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getLongitude() { return longitude; }
    /**
     * Performs the setLongitude operation in this module.
     *
     * @param longitude the longitude input value
     */
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }
    /**
     * Retrieves geohash data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getGeohash() { return geohash; }
    /**
     * Performs the setGeohash operation in this module.
     *
     * @param geohash the geohash input value
     */
    public void setGeohash(String geohash) { this.geohash = geohash; }
    /**
     * Retrieves region data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRegion() { return region; }
    /**
     * Performs the setRegion operation in this module.
     *
     * @param region the region input value
     */
    public void setRegion(String region) { this.region = region; }
    /**
     * Retrieves timezone data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTimezone() { return timezone; }
    /**
     * Performs the setTimezone operation in this module.
     *
     * @param timezone the timezone input value
     */
    public void setTimezone(String timezone) { this.timezone = timezone; }
    /**
     * Retrieves capacity data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getCapacity() { return capacity; }
    /**
     * Performs the setCapacity operation in this module.
     *
     * @param capacity the capacity input value
     */
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
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