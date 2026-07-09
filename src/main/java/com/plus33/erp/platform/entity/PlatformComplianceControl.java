/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformComplianceControl.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformComplianceControlController
 * Related Service   : PlatformComplianceControlService, PlatformComplianceControlServiceImpl
 * Related Repository: PlatformComplianceControlRepository
 * Related Entity    : PlatformComplianceControl
 * Related DTO       : N/A
 * Related Mapper    : PlatformComplianceControlMapper
 * Related DB Table  : platform_compliance_control
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformComplianceControlRepository, PlatformComplianceControlMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_compliance_control'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformComplianceControl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_compliance_control'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_compliance_control}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_compliance_control")
public class PlatformComplianceControl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "framework_id", nullable = false)
    @NotNull
    private Long frameworkId;

    @Column(name = "control_code", nullable = false)
    @NotNull
    @Size(max = 100)
    private String controlCode;

    @Column(name = "control_name", nullable = false)
    @NotNull
    @Size(max = 250)
    private String controlName;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "COMPLIANT";

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
     * Retrieves framework id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getFrameworkId() { return frameworkId; }
    /**
     * Performs the setFrameworkId operation in this module.
     *
     * @param frameworkId the frameworkId input value
     */
    public void setFrameworkId(Long frameworkId) { this.frameworkId = frameworkId; }
    /**
     * Retrieves control code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getControlCode() { return controlCode; }
    /**
     * Performs the setControlCode operation in this module.
     *
     * @param controlCode the controlCode input value
     */
    public void setControlCode(String controlCode) { this.controlCode = controlCode; }
    /**
     * Retrieves control name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getControlName() { return controlName; }
    /**
     * Performs the setControlName operation in this module.
     *
     * @param controlName the controlName input value
     */
    public void setControlName(String controlName) { this.controlName = controlName; }
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
}