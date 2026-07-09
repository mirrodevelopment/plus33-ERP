/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.entity
 * File              : InventoryMovement.java
 * Purpose           : JPA Entity representing a persistent database record in Wms Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryMovementController
 * Related Service   : InventoryMovementService, InventoryMovementServiceImpl
 * Related Repository: InventoryMovementRepository
 * Related Entity    : InventoryMovement
 * Related DTO       : N/A
 * Related Mapper    : InventoryMovementMapper
 * Related DB Table  : inventory_movements
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : InventoryMovementRepository, InventoryMovementMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'inventory_movements'. Defines persistent domain object for Wms Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.wms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code InventoryMovement}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'inventory_movements'.</p>
 *
 * <p><b>Database Table   :</b> {@code inventory_movements}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "inventory_movements")
public class InventoryMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    /**
     * One of: RECEIPT, PUT_AWAY, PICK, ISSUE, TRANSFER_OUT, TRANSFER_IN,
     * ADJUSTMENT_INCREASE, ADJUSTMENT_DECREASE, RETURN, SCRAP,
     * CYCLE_COUNT_ADJUST, CROSS_DOCK, REPLENISHMENT
     */
    @Column(name = "movement_type", nullable = false, length = 50)
    private String movementType;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_location_id")
    private WarehouseLocation fromLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_location_id")
    private WarehouseLocation toLocation;

    @Column(name = "lot_number", length = 50)
    private String lotNumber;

    @Column(name = "serial_number", length = 100)
    private String serialNumber;

    @Column(nullable = false, precision = 18, scale = 6)
    private BigDecimal quantity;

    @Column(name = "unit_id", nullable = false)
    private Long unitId;

    @Column(name = "unit_cost", precision = 18, scale = 6)
    private BigDecimal unitCost;

    @Column(name = "total_cost", precision = 18, scale = 6)
    private BigDecimal totalCost;

    @Column(name = "source_type", length = 30)
    private String sourceType;

    @Column(name = "source_id")
    private Long sourceId;

    @Column(name = "source_line_id")
    private Long sourceLineId;

    /**
     * Idempotency key — prevents duplicate ledger rows if a service call is retried.
     */
    @Column(name = "idempotency_key", length = 100, unique = true)
    private String idempotencyKey;

    @Column(name = "performed_by")
    private Long performedBy;

    /** This record is never updated after insert — movement_at is the single authoritative timestamp. */
    @Column(name = "movement_at", nullable = false, updatable = false)
    private LocalDateTime movementAt = LocalDateTime.now();

    @Column(columnDefinition = "TEXT")
    private String notes;

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
     * Retrieves movement type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getMovementType() { return movementType; }
    /**
     * Performs the setMovementType operation in this module.
     *
     * @param movementType the movementType input value
     */
    public void setMovementType(String movementType) { this.movementType = movementType; }
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
     * Retrieves quantity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getQuantity() { return quantity; }
    /**
     * Performs the setQuantity operation in this module.
     *
     * @param quantity the quantity input value
     */
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
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
     * Retrieves unit cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getUnitCost() { return unitCost; }
    /**
     * Performs the setUnitCost operation in this module.
     *
     * @param unitCost the unitCost input value
     */
    public void setUnitCost(BigDecimal unitCost) { this.unitCost = unitCost; }
    /**
     * Retrieves total cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getTotalCost() { return totalCost; }
    /**
     * Performs the setTotalCost operation in this module.
     *
     * @param totalCost the totalCost input value
     */
    public void setTotalCost(BigDecimal totalCost) { this.totalCost = totalCost; }
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
     * Retrieves idempotency key data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getIdempotencyKey() { return idempotencyKey; }
    /**
     * Performs the setIdempotencyKey operation in this module.
     *
     * @param idempotencyKey the idempotencyKey input value
     */
    public void setIdempotencyKey(String idempotencyKey) { this.idempotencyKey = idempotencyKey; }
    /**
     * Retrieves performed by data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getPerformedBy() { return performedBy; }
    /**
     * Performs the setPerformedBy operation in this module.
     *
     * @param performedBy the performedBy input value
     */
    public void setPerformedBy(Long performedBy) { this.performedBy = performedBy; }
    /**
     * Retrieves movement at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getMovementAt() { return movementAt; }
    /**
     * Performs the setMovementAt operation in this module.
     *
     * @param movementAt the movementAt input value
     */
    public void setMovementAt(LocalDateTime movementAt) { this.movementAt = movementAt; }
    /**
     * Retrieves notes data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getNotes() { return notes; }
    /**
     * Performs the setNotes operation in this module.
     *
     * @param notes the notes input value
     */
    public void setNotes(String notes) { this.notes = notes; }
}