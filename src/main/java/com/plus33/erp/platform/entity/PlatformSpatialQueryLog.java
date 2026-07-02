package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getExecutedQuery() { return executedQuery; }
    public void setExecutedQuery(String executedQuery) { this.executedQuery = executedQuery; }
    public Long getExecutionTimeMs() { return executionTimeMs; }
    public void setExecutionTimeMs(Long executionTimeMs) { this.executionTimeMs = executionTimeMs; }
    public Integer getReturnedRows() { return returnedRows; }
    public void setReturnedRows(Integer returnedRows) { this.returnedRows = returnedRows; }
    public String getSpatialIndexUsed() { return spatialIndexUsed; }
    public void setSpatialIndexUsed(String spatialIndexUsed) { this.spatialIndexUsed = spatialIndexUsed; }
    public String getBoundingBoxWkt() { return boundingBoxWkt; }
    public void setBoundingBoxWkt(String boundingBoxWkt) { this.boundingBoxWkt = boundingBoxWkt; }
    public String getQueryType() { return queryType; }
    public void setQueryType(String queryType) { this.queryType = queryType; }
    public LocalDateTime getQueriedAt() { return queriedAt; }
    public void setQueriedAt(LocalDateTime queriedAt) { this.queriedAt = queriedAt; }
}