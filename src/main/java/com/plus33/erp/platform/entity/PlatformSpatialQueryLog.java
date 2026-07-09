/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformSpatialQueryLog.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformSpatialQueryLogController
 * Related Service   : PlatformSpatialQueryLogService, PlatformSpatialQueryLogServiceImpl
 * Related Repository: PlatformSpatialQueryLogRepository
 * Related Entity    : PlatformSpatialQueryLog
 * Related DTO       : N/A
 * Related Mapper    : PlatformSpatialQueryLogMapper
 * Related DB Table  : platform_spatial_query_log
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformSpatialQueryLogRepository, PlatformSpatialQueryLogMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_spatial_query_log'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformSpatialQueryLog}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_spatial_query_log'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_spatial_query_log}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_spatial_query_log")
public class PlatformSpatialQueryLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "executed_query", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String executedQuery;

    @Column(name = "execution_time_ms", nullable = false)
    @NotNull
    private Long executionTimeMs;

    @Column(name = "returned_rows", nullable = false)
    @NotNull
    private Integer returnedRows;

    @Column(name = "spatial_index_used")
    @Size(max = 100)
    private String spatialIndexUsed;

    @Column(name = "bounding_box_wkt", columnDefinition = "TEXT")
    private String boundingBoxWkt;

    @Column(name = "query_type", nullable = false)
    @NotNull
    @Size(max = 100)
    private String queryType; // BBOX, RADIUS, POLYGON_CONTAINMENT

    @Column(name = "queried_at", nullable = false)
    @NotNull
    private LocalDateTime queriedAt = LocalDateTime.now();

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
     * Retrieves executed query data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getExecutedQuery() { return executedQuery; }
    /**
     * Performs the setExecutedQuery operation in this module.
     *
     * @param executedQuery the executedQuery input value
     */
    public void setExecutedQuery(String executedQuery) { this.executedQuery = executedQuery; }
    /**
     * Retrieves execution time ms data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getExecutionTimeMs() { return executionTimeMs; }
    /**
     * Performs the setExecutionTimeMs operation in this module.
     *
     * @param executionTimeMs the executionTimeMs input value
     */
    public void setExecutionTimeMs(Long executionTimeMs) { this.executionTimeMs = executionTimeMs; }
    /**
     * Retrieves returned rows data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getReturnedRows() { return returnedRows; }
    /**
     * Performs the setReturnedRows operation in this module.
     *
     * @param returnedRows the returnedRows input value
     */
    public void setReturnedRows(Integer returnedRows) { this.returnedRows = returnedRows; }
    /**
     * Retrieves spatial index used data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSpatialIndexUsed() { return spatialIndexUsed; }
    /**
     * Performs the setSpatialIndexUsed operation in this module.
     *
     * @param spatialIndexUsed the spatialIndexUsed input value
     */
    public void setSpatialIndexUsed(String spatialIndexUsed) { this.spatialIndexUsed = spatialIndexUsed; }
    /**
     * Retrieves bounding box wkt data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getBoundingBoxWkt() { return boundingBoxWkt; }
    /**
     * Performs the setBoundingBoxWkt operation in this module.
     *
     * @param boundingBoxWkt the boundingBoxWkt input value
     */
    public void setBoundingBoxWkt(String boundingBoxWkt) { this.boundingBoxWkt = boundingBoxWkt; }
    /**
     * Retrieves query type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getQueryType() { return queryType; }
    /**
     * Performs the setQueryType operation in this module.
     *
     * @param queryType the queryType input value
     */
    public void setQueryType(String queryType) { this.queryType = queryType; }
    /**
     * Retrieves queried at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getQueriedAt() { return queriedAt; }
    /**
     * Performs the setQueriedAt operation in this module.
     *
     * @param queriedAt the queriedAt input value
     */
    public void setQueriedAt(LocalDateTime queriedAt) { this.queriedAt = queriedAt; }
}