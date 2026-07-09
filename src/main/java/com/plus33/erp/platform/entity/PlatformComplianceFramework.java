/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformComplianceFramework.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformComplianceFrameworkController
 * Related Service   : PlatformComplianceFrameworkService, PlatformComplianceFrameworkServiceImpl
 * Related Repository: PlatformComplianceFrameworkRepository
 * Related Entity    : PlatformComplianceFramework
 * Related DTO       : N/A
 * Related Mapper    : PlatformComplianceFrameworkMapper
 * Related DB Table  : platform_compliance_framework
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformComplianceFrameworkRepository, PlatformComplianceFrameworkMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_compliance_framework'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformComplianceFramework}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_compliance_framework'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_compliance_framework}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_compliance_framework")
public class PlatformComplianceFramework {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "framework_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String frameworkCode;

    @Column(name = "framework_name", nullable = false)
    @NotNull
    @Size(max = 250)
    private String frameworkName;

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
     * Retrieves framework code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getFrameworkCode() { return frameworkCode; }
    /**
     * Performs the setFrameworkCode operation in this module.
     *
     * @param frameworkCode the frameworkCode input value
     */
    public void setFrameworkCode(String frameworkCode) { this.frameworkCode = frameworkCode; }
    /**
     * Retrieves framework name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getFrameworkName() { return frameworkName; }
    /**
     * Performs the setFrameworkName operation in this module.
     *
     * @param frameworkName the frameworkName input value
     */
    public void setFrameworkName(String frameworkName) { this.frameworkName = frameworkName; }
}