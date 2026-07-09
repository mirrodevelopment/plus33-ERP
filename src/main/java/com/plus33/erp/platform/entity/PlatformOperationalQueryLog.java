/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformOperationalQueryLog.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformOperationalQueryLogController
 * Related Service   : PlatformOperationalQueryLogService, PlatformOperationalQueryLogServiceImpl
 * Related Repository: PlatformOperationalQueryLogRepository
 * Related Entity    : PlatformOperationalQueryLog
 * Related DTO       : N/A
 * Related Mapper    : PlatformOperationalQueryLogMapper
 * Related DB Table  : platform_operational_query_log
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformOperationalQueryLogRepository, PlatformOperationalQueryLogMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_operational_query_log'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformOperationalQueryLog}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_operational_query_log'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_operational_query_log}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_operational_query_log")
public class PlatformOperationalQueryLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "query_text", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String queryText;

    @Column(name = "parsed_intent", nullable = false)
    @NotNull
    @Size(max = 250)
    private String parsedIntent;

    @Column(name = "execution_plan_json", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String executionPlanJson;

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
     * Retrieves parsed intent data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getParsedIntent() { return parsedIntent; }
    /**
     * Performs the setParsedIntent operation in this module.
     *
     * @param parsedIntent the parsedIntent input value
     */
    public void setParsedIntent(String parsedIntent) { this.parsedIntent = parsedIntent; }
    /**
     * Retrieves execution plan json data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getExecutionPlanJson() { return executionPlanJson; }
    /**
     * Performs the setExecutionPlanJson operation in this module.
     *
     * @param executionPlanJson the executionPlanJson input value
     */
    public void setExecutionPlanJson(String executionPlanJson) { this.executionPlanJson = executionPlanJson; }
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