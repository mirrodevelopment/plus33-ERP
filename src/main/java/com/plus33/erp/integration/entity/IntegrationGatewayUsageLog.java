/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.entity
 * File              : IntegrationGatewayUsageLog.java
 * Purpose           : JPA Entity representing a persistent database record in Integration Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: IntegrationGatewayUsageLogController
 * Related Service   : IntegrationGatewayUsageLogService, IntegrationGatewayUsageLogServiceImpl
 * Related Repository: IntegrationGatewayUsageLogRepository
 * Related Entity    : IntegrationGatewayUsageLog
 * Related DTO       : N/A
 * Related Mapper    : IntegrationGatewayUsageLogMapper
 * Related DB Table  : integration_gateway_usage_log
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : IntegrationGatewayUsageLogRepository, IntegrationGatewayUsageLogMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'integration_gateway_usage_log'. Defines persistent domain object for Integration Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.integration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Integration Module</b>
 *
 * <p><b>Class  :</b> {@code IntegrationGatewayUsageLog}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.integration.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'integration_gateway_usage_log'.</p>
 *
 * <p><b>Database Table   :</b> {@code integration_gateway_usage_log}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "integration_gateway_usage_log")
public class IntegrationGatewayUsageLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "api_key", nullable = false)
    @NotNull
    @Size(max = 100)
    private String apiKey;

    @Column(name = "request_path", nullable = false)
    @NotNull
    @Size(max = 250)
    private String requestPath;

    @Column(name = "http_method", nullable = false)
    @NotNull
    @Size(max = 10)
    private String httpMethod;

    @Column(name = "status_code", nullable = false)
    @NotNull
    private Integer statusCode;

    @Column(name = "response_time_ms", nullable = false)
    @NotNull
    private Long responseTimeMs;

    @Column(name = "called_at", nullable = false, updatable = false)
    @NotNull
    private LocalDateTime calledAt = LocalDateTime.now();

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
     * Retrieves api key data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getApiKey() { return apiKey; }
    /**
     * Performs the setApiKey operation in this module.
     *
     * @param apiKey the apiKey input value
     */
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }
    /**
     * Retrieves request path data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRequestPath() { return requestPath; }
    /**
     * Performs the setRequestPath operation in this module.
     *
     * @param requestPath the requestPath input value
     */
    public void setRequestPath(String requestPath) { this.requestPath = requestPath; }
    /**
     * Retrieves http method data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getHttpMethod() { return httpMethod; }
    /**
     * Performs the setHttpMethod operation in this module.
     *
     * @param httpMethod the httpMethod input value
     */
    public void setHttpMethod(String httpMethod) { this.httpMethod = httpMethod; }
    /**
     * Retrieves status code data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getStatusCode() { return statusCode; }
    /**
     * Performs the setStatusCode operation in this module.
     *
     * @param statusCode the statusCode input value
     */
    public void setStatusCode(Integer statusCode) { this.statusCode = statusCode; }
    /**
     * Retrieves response time ms data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getResponseTimeMs() { return responseTimeMs; }
    /**
     * Performs the setResponseTimeMs operation in this module.
     *
     * @param responseTimeMs the responseTimeMs input value
     */
    public void setResponseTimeMs(Long responseTimeMs) { this.responseTimeMs = responseTimeMs; }
    /**
     * Retrieves a paginated list of called at records.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCalledAt() { return calledAt; }
    /**
     * Performs the setCalledAt operation in this module.
     *
     * @param calledAt the calledAt input value
     */
    public void setCalledAt(LocalDateTime calledAt) { this.calledAt = calledAt; }
}