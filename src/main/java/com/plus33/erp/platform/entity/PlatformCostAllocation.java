/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformCostAllocation.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformCostAllocationController
 * Related Service   : PlatformCostAllocationService, PlatformCostAllocationServiceImpl
 * Related Repository: PlatformCostAllocationRepository
 * Related Entity    : PlatformCostAllocation
 * Related DTO       : N/A
 * Related Mapper    : PlatformCostAllocationMapper
 * Related DB Table  : platform_cost_allocation
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformCostAllocationRepository, PlatformCostAllocationMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_cost_allocation'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformCostAllocation}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_cost_allocation'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_cost_allocation}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_cost_allocation")
public class PlatformCostAllocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "cost_center_id", nullable = false)
    @NotNull
    private Long costCenterId;

    @Column(name = "resource_id", nullable = false)
    @NotNull
    @Size(max = 250)
    private String resourceId;

    @Column(name = "allocation_ratio", nullable = false, precision = 5, scale = 2)
    @NotNull
    private java.math.BigDecimal allocationRatio;

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
     * Retrieves cost center id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCostCenterId() { return costCenterId; }
    /**
     * Performs the setCostCenterId operation in this module.
     *
     * @param costCenterId the costCenterId input value
     */
    public void setCostCenterId(Long costCenterId) { this.costCenterId = costCenterId; }
    /**
     * Retrieves resource id data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getResourceId() { return resourceId; }
    /**
     * Performs the setResourceId operation in this module.
     *
     * @param resourceId the resourceId input value
     */
    public void setResourceId(String resourceId) { this.resourceId = resourceId; }
    /**
     * Retrieves a paginated list of allocation ratio records.
     *
     * @throws ResourceNotFoundException if the entity is not found
     */
    public java.math.BigDecimal getAllocationRatio() { return allocationRatio; }
    /**
     * Performs the setAllocationRatio operation in this module.
     *
     * @param allocationRatio the allocationRatio input value
     */
    public void setAllocationRatio(java.math.BigDecimal allocationRatio) { this.allocationRatio = allocationRatio; }
}