/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.entity
 * File              : InventorySnapshot.java
 * Purpose           : JPA Entity representing a persistent database record in Wms Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventorySnapshotController
 * Related Service   : InventorySnapshotService, InventorySnapshotServiceImpl
 * Related Repository: InventorySnapshotRepository
 * Related Entity    : InventorySnapshot
 * Related DTO       : N/A
 * Related Mapper    : InventorySnapshotMapper
 * Related DB Table  : inventory_snapshots
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : InventorySnapshotRepository, InventorySnapshotMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'inventory_snapshots'. Defines persistent domain object for Wms Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.wms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code InventorySnapshot}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'inventory_snapshots'.</p>
 *
 * <p><b>Database Table   :</b> {@code inventory_snapshots}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "inventory_snapshots")
public class InventorySnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    @Column(name = "location_id", nullable = false)
    private Long locationId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "lot_number", length = 50)
    private String lotNumber;

    @Column(name = "serial_number", length = 100)
    private String serialNumber;

    @Column(name = "snapshot_date", nullable = false)
    private LocalDate snapshotDate;

    @Column(name = "available_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal availableQuantity = BigDecimal.ZERO;

    @Column(name = "reserved_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal reservedQuantity = BigDecimal.ZERO;

    @Column(name = "allocated_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal allocatedQuantity = BigDecimal.ZERO;

    @Column(name = "on_hand_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal onHandQuantity = BigDecimal.ZERO;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

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
     * Retrieves location id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getLocationId() { return locationId; }
    /**
     * Performs the setLocationId operation in this module.
     *
     * @param locationId the locationId input value
     */
    public void setLocationId(Long locationId) { this.locationId = locationId; }
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
     * Retrieves snapshot date data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getSnapshotDate() { return snapshotDate; }
    /**
     * Performs the setSnapshotDate operation in this module.
     *
     * @param snapshotDate the snapshotDate input value
     */
    public void setSnapshotDate(LocalDate snapshotDate) { this.snapshotDate = snapshotDate; }
    /**
     * Retrieves available quantity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getAvailableQuantity() { return availableQuantity; }
    /**
     * Performs the setAvailableQuantity operation in this module.
     *
     * @param availableQuantity the availableQuantity input value
     */
    public void setAvailableQuantity(BigDecimal availableQuantity) { this.availableQuantity = availableQuantity; }
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
     * Retrieves on hand quantity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getOnHandQuantity() { return onHandQuantity; }
    /**
     * Performs the setOnHandQuantity operation in this module.
     *
     * @param onHandQuantity the onHandQuantity input value
     */
    public void setOnHandQuantity(BigDecimal onHandQuantity) { this.onHandQuantity = onHandQuantity; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
}