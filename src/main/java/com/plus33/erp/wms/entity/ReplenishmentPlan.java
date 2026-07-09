/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.entity
 * File              : ReplenishmentPlan.java
 * Purpose           : JPA Entity representing a persistent database record in Wms Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ReplenishmentPlanController
 * Related Service   : ReplenishmentPlanService, ReplenishmentPlanServiceImpl
 * Related Repository: ReplenishmentPlanRepository
 * Related Entity    : ReplenishmentPlan
 * Related DTO       : N/A
 * Related Mapper    : ReplenishmentPlanMapper
 * Related DB Table  : replenishment_plans
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ReplenishmentPlanRepository, ReplenishmentPlanMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'replenishment_plans'. Defines persistent domain object for Wms Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.wms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "replenishment_plans",
       uniqueConstraints = @UniqueConstraint(columnNames = {"warehouse_id", "product_id", "to_location_id"}))
/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code ReplenishmentPlan}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'replenishment_plans'.</p>
 *
 * <p><b>Database Table   :</b> {@code replenishment_plans}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public class ReplenishmentPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "from_zone_id")
    private Long fromZoneId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_location_id")
    private WarehouseLocation toLocation;

    /** MIN_MAX, KANBAN, FORWARD_PICK, DEMAND_BASED, MRP_DRIVEN */
    @Column(nullable = false, length = 30)
    private String strategy = "MIN_MAX";

    @Column(name = "min_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal minQuantity = BigDecimal.ZERO;

    @Column(name = "max_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal maxQuantity = BigDecimal.ZERO;

    @Column(name = "replenish_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal replenishQuantity = BigDecimal.ZERO;

    @Column(name = "unit_id", nullable = false)
    private Long unitId;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    /**
     * Handles the update event or exception in the business workflow.
     *
     */
    @PreUpdate
    protected void onUpdate() { this.updatedAt = LocalDateTime.now(); }

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
     * Retrieves company id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCompanyId() { return companyId; }
    /**
     * Performs the setCompanyId operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     */
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    /**
     * Retrieves warehouse id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getWarehouseId() { return warehouseId; }
    /**
     * Performs the setWarehouseId operation in this module.
     *
     * @param warehouseId the warehouseId input value
     */
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }
    /**
     * Retrieves product id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getProductId() { return productId; }
    /**
     * Performs the setProductId operation in this module.
     *
     * @param productId the productId input value
     */
    public void setProductId(Long productId) { this.productId = productId; }
    /**
     * Retrieves from zone id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getFromZoneId() { return fromZoneId; }
    /**
     * Performs the setFromZoneId operation in this module.
     *
     * @param fromZoneId the fromZoneId input value
     */
    public void setFromZoneId(Long fromZoneId) { this.fromZoneId = fromZoneId; }
    /**
     * Retrieves to location data from the database.
     *
     * @return the WarehouseLocation result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public WarehouseLocation getToLocation() { return toLocation; }
    /**
     * Performs the setToLocation operation in this module.
     *
     * @param toLocation the toLocation input value
     */
    public void setToLocation(WarehouseLocation toLocation) { this.toLocation = toLocation; }
    /**
     * Retrieves strategy data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getStrategy() { return strategy; }
    /**
     * Performs the setStrategy operation in this module.
     *
     * @param strategy the strategy input value
     */
    public void setStrategy(String strategy) { this.strategy = strategy; }
    /**
     * Retrieves min quantity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getMinQuantity() { return minQuantity; }
    /**
     * Performs the setMinQuantity operation in this module.
     *
     * @param minQuantity the minQuantity input value
     */
    public void setMinQuantity(BigDecimal minQuantity) { this.minQuantity = minQuantity; }
    /**
     * Retrieves max quantity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getMaxQuantity() { return maxQuantity; }
    /**
     * Performs the setMaxQuantity operation in this module.
     *
     * @param maxQuantity the maxQuantity input value
     */
    public void setMaxQuantity(BigDecimal maxQuantity) { this.maxQuantity = maxQuantity; }
    /**
     * Retrieves replenish quantity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getReplenishQuantity() { return replenishQuantity; }
    /**
     * Performs the setReplenishQuantity operation in this module.
     *
     * @param replenishQuantity the replenishQuantity input value
     */
    public void setReplenishQuantity(BigDecimal replenishQuantity) { this.replenishQuantity = replenishQuantity; }
    /**
     * Retrieves unit id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getUnitId() { return unitId; }
    /**
     * Performs the setUnitId operation in this module.
     *
     * @param unitId the unitId input value
     */
    public void setUnitId(Long unitId) { this.unitId = unitId; }
    /**
     * Performs the isActive operation in this module.
     *
     * @return true if operation succeeded, false otherwise
     */
    public boolean isActive() { return active; }
    /**
     * Performs the setActive operation in this module.
     *
     * @param active the active input value
     */
    public void setActive(boolean active) { this.active = active; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
    /**
     * Retrieves updated at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    /**
     * Performs the setUpdatedAt operation in this module.
     *
     * @param updatedAt the updatedAt input value
     */
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}