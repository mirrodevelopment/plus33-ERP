/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformCloudCostItem.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformCloudCostItemController
 * Related Service   : PlatformCloudCostItemService, PlatformCloudCostItemServiceImpl
 * Related Repository: PlatformCloudCostItemRepository
 * Related Entity    : PlatformCloudCostItem
 * Related DTO       : N/A
 * Related Mapper    : PlatformCloudCostItemMapper
 * Related DB Table  : platform_cloud_cost_item
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformCloudCostItemRepository, PlatformCloudCostItemMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_cloud_cost_item'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformCloudCostItem}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_cloud_cost_item'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_cloud_cost_item}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_cloud_cost_item")
public class PlatformCloudCostItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "resource_id", nullable = false)
    @NotNull
    @Size(max = 250)
    private String resourceId;

    @Column(nullable = false, precision = 19, scale = 4)
    @NotNull
    private BigDecimal cost;

    @Column(name = "billing_period", nullable = false)
    @NotNull
    @Size(max = 50)
    private String billingPeriod;

    @Column(name = "recorded_at", nullable = false)
    @NotNull
    private LocalDateTime recordedAt = LocalDateTime.now();

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
     * Retrieves cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getCost() { return cost; }
    /**
     * Performs the setCost operation in this module.
     *
     * @param cost the cost input value
     */
    public void setCost(BigDecimal cost) { this.cost = cost; }
    /**
     * Retrieves billing period data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getBillingPeriod() { return billingPeriod; }
    /**
     * Performs the setBillingPeriod operation in this module.
     *
     * @param billingPeriod the billingPeriod input value
     */
    public void setBillingPeriod(String billingPeriod) { this.billingPeriod = billingPeriod; }
    /**
     * Retrieves recorded at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getRecordedAt() { return recordedAt; }
    /**
     * Performs the setRecordedAt operation in this module.
     *
     * @param recordedAt the recordedAt input value
     */
    public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }
}