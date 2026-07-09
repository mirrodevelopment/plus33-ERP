/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformConformanceRule.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformConformanceRuleController
 * Related Service   : PlatformConformanceRuleService, PlatformConformanceRuleServiceImpl
 * Related Repository: PlatformConformanceRuleRepository
 * Related Entity    : PlatformConformanceRule
 * Related DTO       : N/A
 * Related Mapper    : PlatformConformanceRuleMapper
 * Related DB Table  : platform_conformance_rule
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformConformanceRuleRepository, PlatformConformanceRuleMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_conformance_rule'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformConformanceRule}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_conformance_rule'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_conformance_rule}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_conformance_rule")
public class PlatformConformanceRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "process_name", nullable = false)
    @NotNull
    @Size(max = 150)
    private String processName;

    @Column(name = "expected_activity", nullable = false)
    @NotNull
    @Size(max = 150)
    private String expectedActivity;

    @Column(name = "sequence_order", nullable = false)
    @NotNull
    private Integer sequenceOrder;

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
     * Retrieves process name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getProcessName() { return processName; }
    /**
     * Performs the setProcessName operation in this module.
     *
     * @param processName the processName input value
     */
    public void setProcessName(String processName) { this.processName = processName; }
    /**
     * Retrieves expected activity data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getExpectedActivity() { return expectedActivity; }
    /**
     * Performs the setExpectedActivity operation in this module.
     *
     * @param expectedActivity the expectedActivity input value
     */
    public void setExpectedActivity(String expectedActivity) { this.expectedActivity = expectedActivity; }
    /**
     * Retrieves sequence order data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getSequenceOrder() { return sequenceOrder; }
    /**
     * Performs the setSequenceOrder operation in this module.
     *
     * @param sequenceOrder the sequenceOrder input value
     */
    public void setSequenceOrder(Integer sequenceOrder) { this.sequenceOrder = sequenceOrder; }
}