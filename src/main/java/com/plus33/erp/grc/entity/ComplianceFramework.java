/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.entity
 * File              : ComplianceFramework.java
 * Purpose           : JPA Entity representing a persistent database record in Grc Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ComplianceFrameworkController
 * Related Service   : ComplianceFrameworkService, ComplianceFrameworkServiceImpl
 * Related Repository: ComplianceFrameworkRepository
 * Related Entity    : ComplianceFramework
 * Related DTO       : N/A
 * Related Mapper    : ComplianceFrameworkMapper
 * Related DB Table  : grc_compliance_frameworks
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ComplianceFrameworkRepository, ComplianceFrameworkMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'grc_compliance_frameworks'. Defines persistent domain object for Grc Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.grc.entity;

import jakarta.persistence.*;

/**
 * <b>PLUS33 Coffee ERP -- Grc Module</b>
 *
 * <p><b>Class  :</b> {@code ComplianceFramework}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.grc.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'grc_compliance_frameworks'.</p>
 *
 * <p><b>Database Table   :</b> {@code grc_compliance_frameworks}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "grc_compliance_frameworks")
public class ComplianceFramework {
    /**
     * Retrieves id data from the database.
     *
     * @param id the unique database ID of the resource
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "company_id", nullable = false) private Long companyId;
    @Column(nullable = false, unique = true, length = 50) private String code;
    @Column(nullable = false, length = 150) private String name;
    @Column(columnDefinition = "TEXT") private String description;
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    /**
     * Retrieves company id data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCompanyId() { return companyId; } public void setCompanyId(Long v) { this.companyId = v; }
    /**
     * Retrieves code data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCode() { return code; } public void setCode(String v) { this.code = v; }
    /**
     * Retrieves name data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getName() { return name; } public void setName(String v) { this.name = v; }
    /**
     * Retrieves description data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDescription() { return description; } public void setDescription(String v) { this.description = v; }
}