/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.entity
 * File              : ControlMapping.java
 * Purpose           : JPA Entity representing a persistent database record in Grc Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ControlMappingController
 * Related Service   : ControlMappingService, ControlMappingServiceImpl
 * Related Repository: ControlMappingRepository
 * Related Entity    : ControlMapping
 * Related DTO       : N/A
 * Related Mapper    : ControlMappingMapper
 * Related DB Table  : grc_control_mappings
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ControlMappingRepository, ControlMappingMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'grc_control_mappings'. Defines persistent domain object for Grc Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.grc.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "grc_control_mappings",
    uniqueConstraints = @UniqueConstraint(columnNames = {"control_library_id", "framework_id"}))
/**
 * <b>PLUS33 Coffee ERP -- Grc Module</b>
 *
 * <p><b>Class  :</b> {@code ControlMapping}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.grc.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'grc_control_mappings'.</p>
 *
 * <p><b>Database Table   :</b> {@code grc_control_mappings}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public class ControlMapping {
    /**
     * Retrieves id data from the database.
     *
     * @param id the unique database ID of the resource
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "control_library_id", nullable = false) private Long controlLibraryId;
    @Column(name = "framework_id", nullable = false) private Long frameworkId;
    @Column(name = "control_ref", nullable = false, length = 50) private String controlRef;
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    /**
     * Retrieves control library id data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getControlLibraryId() { return controlLibraryId; } public void setControlLibraryId(Long v) { this.controlLibraryId = v; }
    /**
     * Retrieves framework id data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getFrameworkId() { return frameworkId; } public void setFrameworkId(Long v) { this.frameworkId = v; }
    /**
     * Retrieves control ref data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getControlRef() { return controlRef; } public void setControlRef(String v) { this.controlRef = v; }
}