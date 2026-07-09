/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.entity
 * File              : BiWorkloadQueue.java
 * Purpose           : JPA Entity representing a persistent database record in Bi Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiWorkloadQueueController
 * Related Service   : BiWorkloadQueueService, BiWorkloadQueueServiceImpl
 * Related Repository: BiWorkloadQueueRepository
 * Related Entity    : BiWorkloadQueue
 * Related DTO       : N/A
 * Related Mapper    : BiWorkloadQueueMapper
 * Related DB Table  : bi_workload_queue
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BiWorkloadQueueRepository, BiWorkloadQueueMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'bi_workload_queue'. Defines persistent domain object for Bi Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code BiWorkloadQueue}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'bi_workload_queue'.</p>
 *
 * <p><b>Database Table   :</b> {@code bi_workload_queue}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "bi_workload_queue")
public class BiWorkloadQueue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "query_id", nullable = false, unique = true)
    private String queryId;
    @Column(name = "user_id", nullable = false)
    private String userId;
    @Column(name = "pool_name", nullable = false)
    private String poolName = "DEFAULT";
    @Column(nullable = false)
    private Integer priority = 5;
    @Column(name = "query_text", nullable = false)
    private String queryText;
    @Column(nullable = false)
    private String status = "QUEUED";
    @Column(name = "submitted_at", nullable = false)
    private LocalDateTime submittedAt = LocalDateTime.now();
    @Column(name = "started_at")
    private LocalDateTime startedAt;
    @Column(name = "ended_at")
    private LocalDateTime endedAt;

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
     * Retrieves query id data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getQueryId() { return queryId; }
    /**
     * Performs the setQueryId operation in this module.
     *
     * @param queryId the queryId input value
     */
    public void setQueryId(String queryId) { this.queryId = queryId; }
    /**
     * Retrieves user id data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getUserId() { return userId; }
    /**
     * Performs the setUserId operation in this module.
     *
     * @param userId authenticated user identifier
     */
    public void setUserId(String userId) { this.userId = userId; }
    /**
     * Retrieves pool name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPoolName() { return poolName; }
    /**
     * Performs the setPoolName operation in this module.
     *
     * @param poolName the poolName input value
     */
    public void setPoolName(String poolName) { this.poolName = poolName; }
    /**
     * Retrieves priority data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getPriority() { return priority; }
    /**
     * Performs the setPriority operation in this module.
     *
     * @param priority the priority input value
     */
    public void setPriority(Integer priority) { this.priority = priority; }
    /**
     * Retrieves query text data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getQueryText() { return queryText; }
    /**
     * Performs the setQueryText operation in this module.
     *
     * @param queryText the queryText input value
     */
    public void setQueryText(String queryText) { this.queryText = queryText; }
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
     * Retrieves submitted at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    /**
     * Performs the setSubmittedAt operation in this module.
     *
     * @param submittedAt the submittedAt input value
     */
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
    /**
     * Retrieves started at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getStartedAt() { return startedAt; }
    /**
     * Performs the setStartedAt operation in this module.
     *
     * @param startedAt the startedAt input value
     */
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
    /**
     * Retrieves ended at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getEndedAt() { return endedAt; }
    /**
     * Performs the setEndedAt operation in this module.
     *
     * @param endedAt the endedAt input value
     */
    public void setEndedAt(LocalDateTime endedAt) { this.endedAt = endedAt; }
}