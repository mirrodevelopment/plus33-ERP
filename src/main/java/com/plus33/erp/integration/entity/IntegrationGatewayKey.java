/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.entity
 * File              : IntegrationGatewayKey.java
 * Purpose           : JPA Entity representing a persistent database record in Integration Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: IntegrationGatewayKeyController
 * Related Service   : IntegrationGatewayKeyService, IntegrationGatewayKeyServiceImpl
 * Related Repository: IntegrationGatewayKeyRepository
 * Related Entity    : IntegrationGatewayKey
 * Related DTO       : N/A
 * Related Mapper    : IntegrationGatewayKeyMapper
 * Related DB Table  : integration_gateway_key
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : IntegrationGatewayKeyRepository, IntegrationGatewayKeyMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'integration_gateway_key'. Defines persistent domain object for Integration Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.integration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Integration Module</b>
 *
 * <p><b>Class  :</b> {@code IntegrationGatewayKey}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.integration.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'integration_gateway_key'.</p>
 *
 * <p><b>Database Table   :</b> {@code integration_gateway_key}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "integration_gateway_key")
public class IntegrationGatewayKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "api_key", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String apiKey;

    @Column(name = "partner_code", nullable = false)
    @NotNull
    @Size(max = 100)
    private String partnerCode;

    @Column(name = "rate_limit_per_min", nullable = false)
    @NotNull
    private Integer rateLimitPerMin = 60;

    @Column(name = "quota_per_day", nullable = false)
    @NotNull
    private Integer quotaPerDay = 10000;

    @Column(name = "allowed_routes", nullable = false)
    @NotNull
    @Size(max = 500)
    private String allowedRoutes = "*";

    @Column(nullable = false)
    @NotNull
    private Boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

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
     * Retrieves partner code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPartnerCode() { return partnerCode; }
    /**
     * Performs the setPartnerCode operation in this module.
     *
     * @param partnerCode the partnerCode input value
     */
    public void setPartnerCode(String partnerCode) { this.partnerCode = partnerCode; }
    /**
     * Retrieves rate limit per min data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getRateLimitPerMin() { return rateLimitPerMin; }
    /**
     * Performs the setRateLimitPerMin operation in this module.
     *
     * @param rateLimitPerMin the rateLimitPerMin input value
     */
    public void setRateLimitPerMin(Integer rateLimitPerMin) { this.rateLimitPerMin = rateLimitPerMin; }
    /**
     * Retrieves quota per day data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getQuotaPerDay() { return quotaPerDay; }
    /**
     * Performs the setQuotaPerDay operation in this module.
     *
     * @param quotaPerDay the quotaPerDay input value
     */
    public void setQuotaPerDay(Integer quotaPerDay) { this.quotaPerDay = quotaPerDay; }
    /**
     * Retrieves a paginated list of allowed routes records.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getAllowedRoutes() { return allowedRoutes; }
    /**
     * Performs the setAllowedRoutes operation in this module.
     *
     * @param allowedRoutes the allowedRoutes input value
     */
    public void setAllowedRoutes(String allowedRoutes) { this.allowedRoutes = allowedRoutes; }
    /**
     * Retrieves active data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getActive() { return active; }
    /**
     * Performs the setActive operation in this module.
     *
     * @param active the active input value
     */
    public void setActive(Boolean active) { this.active = active; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
    /**
     * Performs the setCreatedAt operation in this module.
     *
     * @param createdAt the createdAt input value
     */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}