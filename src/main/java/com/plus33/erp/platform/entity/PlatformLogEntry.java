/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformLogEntry.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformLogEntryController
 * Related Service   : PlatformLogEntryService, PlatformLogEntryServiceImpl
 * Related Repository: PlatformLogEntryRepository
 * Related Entity    : PlatformLogEntry
 * Related DTO       : N/A
 * Related Mapper    : PlatformLogEntryMapper
 * Related DB Table  : platform_log_entry
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformLogEntryRepository, PlatformLogEntryMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_log_entry'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformLogEntry}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_log_entry'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_log_entry}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_log_entry")
public class PlatformLogEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trace_id")
    @Size(max = 100)
    private String traceId;

    @Column(name = "span_id")
    @Size(max = 100)
    private String spanId;

    @Column(name = "service_name", nullable = false)
    @NotNull
    @Size(max = 100)
    private String serviceName;

    @Column(name = "node_id", nullable = false)
    @NotNull
    @Size(max = 100)
    private String nodeId;

    @Column(name = "log_level", nullable = false)
    @NotNull
    @Size(max = 50)
    private String logLevel;

    @Column(nullable = false)
    @NotNull
    @Size(max = 250)
    private String logger;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String message;

    @Column(name = "json_payload", columnDefinition = "TEXT")
    private String jsonPayload;

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
     * Retrieves service name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getServiceName() { return serviceName; }
    /**
     * Performs the setServiceName operation in this module.
     *
     * @param serviceName the serviceName input value
     */
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    /**
     * Retrieves node id data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getNodeId() { return nodeId; }
    /**
     * Performs the setNodeId operation in this module.
     *
     * @param nodeId the nodeId input value
     */
    public void setNodeId(String nodeId) { this.nodeId = nodeId; }
    /**
     * Retrieves log level data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getLogLevel() { return logLevel; }
    /**
     * Performs the setLogLevel operation in this module.
     *
     * @param logLevel the logLevel input value
     */
    public void setLogLevel(String logLevel) { this.logLevel = logLevel; }
    /**
     * Retrieves logger data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getLogger() { return logger; }
    /**
     * Performs the setLogger operation in this module.
     *
     * @param logger the logger input value
     */
    public void setLogger(String logger) { this.logger = logger; }
    /**
     * Retrieves message data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getMessage() { return message; }
    /**
     * Performs the setMessage operation in this module.
     *
     * @param message the message input value
     */
    public void setMessage(String message) { this.message = message; }
    /**
     * Retrieves json payload data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getJsonPayload() { return jsonPayload; }
    /**
     * Performs the setJsonPayload operation in this module.
     *
     * @param jsonPayload the jsonPayload input value
     */
    public void setJsonPayload(String jsonPayload) { this.jsonPayload = jsonPayload; }
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