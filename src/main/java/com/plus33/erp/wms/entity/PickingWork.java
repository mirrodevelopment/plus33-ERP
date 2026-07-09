/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.entity
 * File              : PickingWork.java
 * Purpose           : JPA Entity representing a persistent database record in Wms Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PickingWorkController
 * Related Service   : PickingWorkService, PickingWorkServiceImpl
 * Related Repository: PickingWorkRepository
 * Related Entity    : PickingWork
 * Related DTO       : N/A
 * Related Mapper    : PickingWorkMapper
 * Related DB Table  : picking_work
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PickingWorkRepository, PickingWorkMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'picking_work'. Defines persistent domain object for Wms Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.wms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code PickingWork}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'picking_work'.</p>
 *
 * <p><b>Database Table   :</b> {@code picking_work}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "picking_work")
public class PickingWork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wave_id", nullable = false)
    private Wave wave;

    @Column(name = "source_type", nullable = false, length = 30)
    private String sourceType = "SALES_ORDER";
    // SALES_ORDER, PRODUCTION_ORDER, TRANSFER, SERVICE_ORDER

    @Column(name = "source_id")
    private Long sourceId;

    @Column(name = "source_line_id")
    private Long sourceLineId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "lot_number", length = 50)
    private String lotNumber;

    @Column(name = "serial_number", length = 100)
    private String serialNumber;

    @Column(name = "pick_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal pickQuantity;

    @Column(name = "picked_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal pickedQuantity = BigDecimal.ZERO;

    @Column(name = "unit_id", nullable = false)
    private Long unitId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_location_id")
    private WarehouseLocation fromLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_location_id")
    private WarehouseLocation toLocation;

    @Column(name = "strategy_used", length = 50)
    private String strategyUsed;

    @Column(nullable = false, length = 30)
    private String status = "PENDING";
    // PENDING, ASSIGNED, IN_PROGRESS, PARTIALLY_PICKED, COMPLETED, CANCELLED, SHORTED

    @Column(name = "assigned_to")
    private Long assignedTo;

    /**
     * Optimistic lock — detects concurrent picking of the same work record.
     * Hibernate will throw OptimisticLockException if two pickers race on the same row.
     */
    @Version
    @Column(nullable = false)
    private Long version = 0L;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

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
     * Retrieves wave data from the database.
     *
     * @return the Wave result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Wave getWave() { return wave; }
    /**
     * Performs the setWave operation in this module.
     *
     * @param wave the wave input value
     */
    public void setWave(Wave wave) { this.wave = wave; }
    /**
     * Retrieves source type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSourceType() { return sourceType; }
    /**
     * Performs the setSourceType operation in this module.
     *
     * @param sourceType the sourceType input value
     */
    public void setSourceType(String sourceType) { this.sourceType = sourceType; }
    /**
     * Retrieves source id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getSourceId() { return sourceId; }
    /**
     * Performs the setSourceId operation in this module.
     *
     * @param sourceId the sourceId input value
     */
    public void setSourceId(Long sourceId) { this.sourceId = sourceId; }
    /**
     * Retrieves source line id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getSourceLineId() { return sourceLineId; }
    /**
     * Performs the setSourceLineId operation in this module.
     *
     * @param sourceLineId the sourceLineId input value
     */
    public void setSourceLineId(Long sourceLineId) { this.sourceLineId = sourceLineId; }
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
     * Retrieves lot number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getLotNumber() { return lotNumber; }
    /**
     * Performs the setLotNumber operation in this module.
     *
     * @param lotNumber the lotNumber input value
     */
    public void setLotNumber(String lotNumber) { this.lotNumber = lotNumber; }
    /**
     * Retrieves serial number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSerialNumber() { return serialNumber; }
    /**
     * Performs the setSerialNumber operation in this module.
     *
     * @param serialNumber the serialNumber input value
     */
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
    /**
     * Retrieves pick quantity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getPickQuantity() { return pickQuantity; }
    /**
     * Performs the setPickQuantity operation in this module.
     *
     * @param pickQuantity the pickQuantity input value
     */
    public void setPickQuantity(BigDecimal pickQuantity) { this.pickQuantity = pickQuantity; }
    /**
     * Retrieves picked quantity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getPickedQuantity() { return pickedQuantity; }
    /**
     * Performs the setPickedQuantity operation in this module.
     *
     * @param pickedQuantity the pickedQuantity input value
     */
    public void setPickedQuantity(BigDecimal pickedQuantity) { this.pickedQuantity = pickedQuantity; }
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
     * Retrieves from location data from the database.
     *
     * @return the WarehouseLocation result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public WarehouseLocation getFromLocation() { return fromLocation; }
    /**
     * Performs the setFromLocation operation in this module.
     *
     * @param fromLocation the fromLocation input value
     */
    public void setFromLocation(WarehouseLocation fromLocation) { this.fromLocation = fromLocation; }
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
     * Retrieves strategy used data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getStrategyUsed() { return strategyUsed; }
    /**
     * Performs the setStrategyUsed operation in this module.
     *
     * @param strategyUsed the strategyUsed input value
     */
    public void setStrategyUsed(String strategyUsed) { this.strategyUsed = strategyUsed; }
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
    /**
     * Retrieves assigned to data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getAssignedTo() { return assignedTo; }
    /**
     * Performs the setAssignedTo operation in this module.
     *
     * @param assignedTo the assignedTo input value
     */
    public void setAssignedTo(Long assignedTo) { this.assignedTo = assignedTo; }
    /**
     * Retrieves version data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getVersion() { return version; }
    /**
     * Performs the setVersion operation in this module.
     *
     * @param version the version input value
     */
    public void setVersion(Long version) { this.version = version; }
    /**
     * Retrieves started at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getStartedAt() { return startedAt; }
    /**
     * Performs the setStartedAt operation in this module.
     *
     * @param startedAt the startedAt input value
     */
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
    /**
     * Retrieves completed at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCompletedAt() { return completedAt; }
    /**
     * Performs the setCompletedAt operation in this module.
     *
     * @param completedAt the completedAt input value
     */
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
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