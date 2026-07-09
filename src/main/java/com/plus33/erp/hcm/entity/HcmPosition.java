/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Hcm Module
 * Package           : com.plus33.erp.hcm.entity
 * File              : HcmPosition.java
 * Purpose           : JPA Entity representing a persistent database record in Hcm Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: HcmPositionController
 * Related Service   : HcmPositionService, HcmPositionServiceImpl
 * Related Repository: HcmPositionRepository
 * Related Entity    : HcmPosition
 * Related DTO       : N/A
 * Related Mapper    : HcmPositionMapper
 * Related DB Table  : hcm_positions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : HcmPositionRepository, HcmPositionMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'hcm_positions'. Defines persistent domain object for Hcm Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.hcm.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * <b>PLUS33 Coffee ERP -- Hcm Module</b>
 *
 * <p><b>Class  :</b> {@code HcmPosition}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.hcm.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'hcm_positions'.</p>
 *
 * <p><b>Database Table   :</b> {@code hcm_positions}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "hcm_positions")
public class HcmPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "department_id", nullable = false)
    private Long departmentId;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(name = "effective_from", nullable = false)
    private LocalDate effectiveFrom;

    @Column(name = "effective_to", nullable = false)
    private LocalDate effectiveTo;

    @Column(name = "version_number", nullable = false)
    private Integer versionNumber = 1;

    @Column(name = "is_current", nullable = false)
    private Boolean isCurrent = true;

    // Getters and setters
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
     * Retrieves department id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getDepartmentId() { return departmentId; }
    /**
     * Performs the setDepartmentId operation in this module.
     *
     * @param departmentId the departmentId input value
     */
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
    /**
     * Retrieves code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCode() { return code; }
    /**
     * Performs the setCode operation in this module.
     *
     * @param code the code input value
     */
    public void setCode(String code) { this.code = code; }
    /**
     * Retrieves title data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTitle() { return title; }
    /**
     * Performs the setTitle operation in this module.
     *
     * @param title the title input value
     */
    public void setTitle(String title) { this.title = title; }
    /**
     * Retrieves effective from data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getEffectiveFrom() { return effectiveFrom; }
    /**
     * Performs the setEffectiveFrom operation in this module.
     *
     * @param effectiveFrom the effectiveFrom input value
     */
    public void setEffectiveFrom(LocalDate effectiveFrom) { this.effectiveFrom = effectiveFrom; }
    /**
     * Retrieves effective to data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getEffectiveTo() { return effectiveTo; }
    /**
     * Performs the setEffectiveTo operation in this module.
     *
     * @param effectiveTo the effectiveTo input value
     */
    public void setEffectiveTo(LocalDate effectiveTo) { this.effectiveTo = effectiveTo; }
    /**
     * Retrieves version number data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getVersionNumber() { return versionNumber; }
    /**
     * Performs the setVersionNumber operation in this module.
     *
     * @param versionNumber the versionNumber input value
     */
    public void setVersionNumber(Integer versionNumber) { this.versionNumber = versionNumber; }
    /**
     * Retrieves is current data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getIsCurrent() { return isCurrent; }
    /**
     * Performs the setIsCurrent operation in this module.
     *
     * @param isCurrent the isCurrent input value
     */
    public void setIsCurrent(Boolean isCurrent) { this.isCurrent = isCurrent; }
}