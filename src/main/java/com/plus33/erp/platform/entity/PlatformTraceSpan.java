/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformTraceSpan.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformTraceSpanController
 * Related Service   : PlatformTraceSpanService, PlatformTraceSpanServiceImpl
 * Related Repository: PlatformTraceSpanRepository
 * Related Entity    : PlatformTraceSpan
 * Related DTO       : N/A
 * Related Mapper    : PlatformTraceSpanMapper
 * Related DB Table  : platform_trace_span
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformTraceSpanRepository, PlatformTraceSpanMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_trace_span'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformTraceSpan}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_trace_span'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_trace_span}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_trace_span")
public class PlatformTraceSpan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trace_id", nullable = false)
    @NotNull
    @Size(max = 100)
    private String traceId;

    @Column(name = "span_id", nullable = false)
    @NotNull
    @Size(max = 100)
    private String spanId;

    @Column(name = "parent_span_id")
    @Size(max = 100)
    private String parentSpanId;

    @Column(name = "operation_name", nullable = false)
    @NotNull
    @Size(max = 250)
    private String operationName;

    @Column(name = "duration_ms", nullable = false)
    @NotNull
    private Long durationMs;

    @Column(nullable = false)
    @NotNull
    private LocalDateTime timestamp = LocalDateTime.now();

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
     * Retrieves trace id data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTraceId() { return traceId; }
    /**
     * Performs the setTraceId operation in this module.
     *
     * @param traceId the traceId input value
     */
    public void setTraceId(String traceId) { this.traceId = traceId; }
    /**
     * Retrieves span id data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSpanId() { return spanId; }
    /**
     * Performs the setSpanId operation in this module.
     *
     * @param spanId the spanId input value
     */
    public void setSpanId(String spanId) { this.spanId = spanId; }
    /**
     * Retrieves parent span id data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getParentSpanId() { return parentSpanId; }
    /**
     * Performs the setParentSpanId operation in this module.
     *
     * @param parentSpanId the parentSpanId input value
     */
    public void setParentSpanId(String parentSpanId) { this.parentSpanId = parentSpanId; }
    /**
     * Retrieves operation name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getOperationName() { return operationName; }
    /**
     * Performs the setOperationName operation in this module.
     *
     * @param operationName the operationName input value
     */
    public void setOperationName(String operationName) { this.operationName = operationName; }
    /**
     * Retrieves duration ms data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getDurationMs() { return durationMs; }
    /**
     * Performs the setDurationMs operation in this module.
     *
     * @param durationMs the durationMs input value
     */
    public void setDurationMs(Long durationMs) { this.durationMs = durationMs; }
    /**
     * Retrieves timestamp data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getTimestamp() { return timestamp; }
    /**
     * Performs the setTimestamp operation in this module.
     *
     * @param timestamp the timestamp input value
     */
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}