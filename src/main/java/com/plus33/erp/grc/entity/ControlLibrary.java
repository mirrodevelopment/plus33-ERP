/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.entity
 * File              : ControlLibrary.java
 * Purpose           : JPA Entity representing a persistent database record in Grc Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ControlLibraryController
 * Related Service   : ControlLibraryService, ControlLibraryServiceImpl
 * Related Repository: ControlLibraryRepository
 * Related Entity    : ControlLibrary
 * Related DTO       : N/A
 * Related Mapper    : ControlLibraryMapper
 * Related DB Table  : grc_control_library
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ControlLibraryRepository, ControlLibraryMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'grc_control_library'. Defines persistent domain object for Grc Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.grc.entity;

import jakarta.persistence.*;

/**
 * <b>PLUS33 Coffee ERP -- Grc Module</b>
 *
 * <p><b>Class  :</b> {@code ControlLibrary}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.grc.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'grc_control_library'.</p>
 *
 * <p><b>Database Table   :</b> {@code grc_control_library}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "grc_control_library")
public class ControlLibrary {
    /**
     * Retrieves id data from the database.
     *
     * @param id the unique database ID of the resource
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "company_id", nullable = false) private Long companyId;
    @Column(name = "control_code", nullable = false, unique = true, length = 50) private String controlCode;
    @Column(nullable = false, length = 200) private String name;
    @Column(columnDefinition = "TEXT") private String description;
    @Column(nullable = false, length = 30) private String status = "DRAFT";
    @Column(name = "owner_id") private Long ownerId;
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
     * Retrieves control code data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getControlCode() { return controlCode; } public void setControlCode(String v) { this.controlCode = v; }
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
    /**
     * Retrieves status data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getStatus() { return status; } public void setStatus(String v) { this.status = v; }
    /**
     * Retrieves owner id data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getOwnerId() { return ownerId; } public void setOwnerId(Long v) { this.ownerId = v; }
}