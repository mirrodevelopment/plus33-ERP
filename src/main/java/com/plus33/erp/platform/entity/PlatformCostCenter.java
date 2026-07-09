/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformCostCenter.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformCostCenterController
 * Related Service   : PlatformCostCenterService, PlatformCostCenterServiceImpl
 * Related Repository: PlatformCostCenterRepository
 * Related Entity    : PlatformCostCenter
 * Related DTO       : N/A
 * Related Mapper    : PlatformCostCenterMapper
 * Related DB Table  : platform_cost_center
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformCostCenterRepository, PlatformCostCenterMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_cost_center'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformCostCenter}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_cost_center'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_cost_center}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_cost_center")
public class PlatformCostCenter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "center_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String centerCode;

    @Column(name = "center_name", nullable = false)
    @NotNull
    @Size(max = 150)
    private String centerName;

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
     * Retrieves center code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCenterCode() { return centerCode; }
    /**
     * Performs the setCenterCode operation in this module.
     *
     * @param centerCode the centerCode input value
     */
    public void setCenterCode(String centerCode) { this.centerCode = centerCode; }
    /**
     * Retrieves center name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCenterName() { return centerName; }
    /**
     * Performs the setCenterName operation in this module.
     *
     * @param centerName the centerName input value
     */
    public void setCenterName(String centerName) { this.centerName = centerName; }
}