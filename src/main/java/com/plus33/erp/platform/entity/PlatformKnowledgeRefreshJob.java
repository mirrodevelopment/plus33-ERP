/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformKnowledgeRefreshJob.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformKnowledgeRefreshJobController
 * Related Service   : PlatformKnowledgeRefreshJobService, PlatformKnowledgeRefreshJobServiceImpl
 * Related Repository: PlatformKnowledgeRefreshJobRepository
 * Related Entity    : PlatformKnowledgeRefreshJob
 * Related DTO       : N/A
 * Related Mapper    : PlatformKnowledgeRefreshJobMapper
 * Related DB Table  : platform_knowledge_refresh_job
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformKnowledgeRefreshJobRepository, PlatformKnowledgeRefreshJobMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_knowledge_refresh_job'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformKnowledgeRefreshJob}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_knowledge_refresh_job'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_knowledge_refresh_job}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_knowledge_refresh_job")
public class PlatformKnowledgeRefreshJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source_id", nullable = false)
    @NotNull
    private Long sourceId;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "PENDING";

    @Column(columnDefinition = "TEXT")
    private String logs;

    @Column(name = "triggered_at", nullable = false)
    @NotNull
    private LocalDateTime triggeredAt = LocalDateTime.now();

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
     * Retrieves source id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getSourceId() { return sourceId; }
    /**
     * Performs the setSourceId operation in this module.
     *
     * @param sourceId the sourceId input value
     */
    public void setSourceId(Long sourceId) { this.sourceId = sourceId; }
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
     * Retrieves logs data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getLogs() { return logs; }
    /**
     * Performs the setLogs operation in this module.
     *
     * @param logs the logs input value
     */
    public void setLogs(String logs) { this.logs = logs; }
    /**
     * Retrieves triggered at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getTriggeredAt() { return triggeredAt; }
    /**
     * Performs the setTriggeredAt operation in this module.
     *
     * @param triggeredAt the triggeredAt input value
     */
    public void setTriggeredAt(LocalDateTime triggeredAt) { this.triggeredAt = triggeredAt; }
}