/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.entity
 * File              : InventoryReservation.java
 * Purpose           : JPA Entity representing a persistent database record in Wms Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryReservationController
 * Related Service   : InventoryReservationService, InventoryReservationServiceImpl
 * Related Repository: InventoryReservationRepository
 * Related Entity    : InventoryReservation
 * Related DTO       : N/A
 * Related Mapper    : InventoryReservationMapper
 * Related DB Table  : inventory_reservations
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : InventoryReservationRepository, InventoryReservationMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'inventory_reservations'. Defines persistent domain object for Wms Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.wms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code InventoryReservation}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'inventory_reservations'.</p>
 *
 * <p><b>Database Table   :</b> {@code inventory_reservations}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "inventory_reservations")
public class InventoryReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private WarehouseLocation location;

    @Column(name = "lot_number", length = 50)
    private String lotNumber;

    @Column(name = "serial_number", length = 100)
    private String serialNumber;

    @Column(name = "reserved_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal reservedQuantity;

    @Column(name = "allocated_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal allocatedQuantity = BigDecimal.ZERO;

    @Column(name = "picked_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal pickedQuantity = BigDecimal.ZERO;

    /** Lifecycle: CREATED → ALLOCATED → PICKED → CONSUMED → RELEASED / EXPIRED */
    @Column(nullable = false, length = 30)
    private String status = "CREATED";

    /** Source type: SALES_ORDER, PRODUCTION_ORDER, TRANSFER, SERVICE_ORDER, PROJECT, WAVE */
    @Column(name = "source_type", nullable = false, length = 30)
    private String sourceType;

    @Column(name = "source_id")
    private Long sourceId;

    @Column(name = "source_line_id")
    private Long sourceLineId;

    @Column(name = "wave_id")
    private Long waveId;

    @Column(name = "expiry_at")
    private LocalDateTime expiryAt;

    @Column(name = "idempotency_key", length = 100)
    private String idempotencyKey;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "released_by")
    private Long releasedBy;

    @Column(name = "released_at")
    private LocalDateTime releasedAt;

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
     * Retrieves location data from the database.
     *
     * @return the WarehouseLocation result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public WarehouseLocation getLocation() { return location; }
    /**
     * Performs the setLocation operation in this module.
     *
     * @param location the location input value
     */
    public void setLocation(WarehouseLocation location) { this.location = location; }
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
     * Retrieves reserved quantity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getReservedQuantity() { return reservedQuantity; }
    /**
     * Performs the setReservedQuantity operation in this module.
     *
     * @param reservedQuantity the reservedQuantity input value
     */
    public void setReservedQuantity(BigDecimal reservedQuantity) { this.reservedQuantity = reservedQuantity; }
    /**
     * Retrieves a paginated list of allocated quantity records.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getAllocatedQuantity() { return allocatedQuantity; }
    /**
     * Performs the setAllocatedQuantity operation in this module.
     *
     * @param allocatedQuantity the allocatedQuantity input value
     */
    public void setAllocatedQuantity(BigDecimal allocatedQuantity) { this.allocatedQuantity = allocatedQuantity; }
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
     * Retrieves wave id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getWaveId() { return waveId; }
    /**
     * Performs the setWaveId operation in this module.
     *
     * @param waveId the waveId input value
     */
    public void setWaveId(Long waveId) { this.waveId = waveId; }
    /**
     * Retrieves expiry at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getExpiryAt() { return expiryAt; }
    /**
     * Performs the setExpiryAt operation in this module.
     *
     * @param expiryAt the expiryAt input value
     */
    public void setExpiryAt(LocalDateTime expiryAt) { this.expiryAt = expiryAt; }
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
    /**
     * Retrieves created by data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCreatedBy() { return createdBy; }
    /**
     * Performs the setCreatedBy operation in this module.
     *
     * @param createdBy the createdBy input value
     */
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    /**
     * Retrieves released by data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getReleasedBy() { return releasedBy; }
    /**
     * Performs the setReleasedBy operation in this module.
     *
     * @param releasedBy the releasedBy input value
     */
    public void setReleasedBy(Long releasedBy) { this.releasedBy = releasedBy; }
    /**
     * Retrieves released at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getReleasedAt() { return releasedAt; }
    /**
     * Performs the setReleasedAt operation in this module.
     *
     * @param releasedAt the releasedAt input value
     */
    public void setReleasedAt(LocalDateTime releasedAt) { this.releasedAt = releasedAt; }
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