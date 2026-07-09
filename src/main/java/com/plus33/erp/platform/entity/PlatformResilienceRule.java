/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformResilienceRule.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformResilienceRuleController
 * Related Service   : PlatformResilienceRuleService, PlatformResilienceRuleServiceImpl
 * Related Repository: PlatformResilienceRuleRepository
 * Related Entity    : PlatformResilienceRule
 * Related DTO       : N/A
 * Related Mapper    : PlatformResilienceRuleMapper
 * Related DB Table  : platform_resilience_rule
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformResilienceRuleRepository, PlatformResilienceRuleMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_resilience_rule'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformResilienceRule}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_resilience_rule'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_resilience_rule}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_resilience_rule")
public class PlatformResilienceRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "service_name", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String serviceName;

    @Column(name = "rate_limit_rpm", nullable = false)
    @NotNull
    private Integer rateLimitRpm = 600;

    @Column(name = "timeout_ms", nullable = false)
    @NotNull
    private Integer timeoutMs = 3000;

    @Column(name = "retry_attempts", nullable = false)
    @NotNull
    private Integer retryAttempts = 3;

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
     * Retrieves rate limit rpm data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getRateLimitRpm() { return rateLimitRpm; }
    /**
     * Performs the setRateLimitRpm operation in this module.
     *
     * @param rateLimitRpm the rateLimitRpm input value
     */
    public void setRateLimitRpm(Integer rateLimitRpm) { this.rateLimitRpm = rateLimitRpm; }
    /**
     * Retrieves timeout ms data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getTimeoutMs() { return timeoutMs; }
    /**
     * Performs the setTimeoutMs operation in this module.
     *
     * @param timeoutMs the timeoutMs input value
     */
    public void setTimeoutMs(Integer timeoutMs) { this.timeoutMs = timeoutMs; }
    /**
     * Retrieves retry attempts data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getRetryAttempts() { return retryAttempts; }
    /**
     * Performs the setRetryAttempts operation in this module.
     *
     * @param retryAttempts the retryAttempts input value
     */
    public void setRetryAttempts(Integer retryAttempts) { this.retryAttempts = retryAttempts; }
}