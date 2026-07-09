/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.entity
 * File              : Shipment.java
 * Purpose           : JPA Entity representing a persistent database record in Wms Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ShipmentController
 * Related Service   : ShipmentService, ShipmentServiceImpl
 * Related Repository: ShipmentRepository
 * Related Entity    : Shipment
 * Related DTO       : N/A
 * Related Mapper    : ShipmentMapper
 * Related DB Table  : shipments
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ShipmentRepository, ShipmentMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'shipments'. Defines persistent domain object for Wms Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.wms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "shipments",
       uniqueConstraints = @UniqueConstraint(columnNames = {"company_id", "shipment_number"}))
/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code Shipment}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'shipments'.</p>
 *
 * <p><b>Database Table   :</b> {@code shipments}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    @Column(name = "shipment_number", nullable = false, length = 50)
    private String shipmentNumber;

    @Column(name = "wave_id")
    private Long waveId;

    @Column(name = "carrier_id")
    private Long carrierId;

    @Column(nullable = false, length = 30)
    private String status = "DRAFT";
    // DRAFT, PACKED, LOADED, DISPATCHED, IN_TRANSIT, DELIVERED, RETURNED, CANCELLED

    @Column(name = "ship_method", length = 50)
    private String shipMethod;

    @Column(name = "tracking_number", length = 100)
    private String trackingNumber;

    @Column(name = "pro_number", length = 100)
    private String proNumber;

    @Column(name = "estimated_delivery")
    private LocalDate estimatedDelivery;

    @Column(name = "actual_delivery")
    private LocalDateTime actualDelivery;

    @Column(name = "total_weight_kg", precision = 12, scale = 3)
    private BigDecimal totalWeightKg;

    @Column(name = "total_volume_m3", precision = 12, scale = 6)
    private BigDecimal totalVolumeM3;

    @Column(name = "freight_cost", precision = 18, scale = 2)
    private BigDecimal freightCost;

    @Column(name = "freight_currency", nullable = false, length = 3)
    private String freightCurrency = "EUR";

    @Column(name = "pod_reference", length = 100)
    private String podReference;

    @Column(name = "pod_signed_by", length = 100)
    private String podSignedBy;

    @Column(name = "pod_at")
    private LocalDateTime podAt;

    @Column(name = "dock_door_id")
    private Long dockDoorId;

    @Column(name = "dispatched_by")
    private Long dispatchedBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<ShipmentLine> lines = new java.util.ArrayList<>();

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
     * Retrieves shipment number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getShipmentNumber() { return shipmentNumber; }
    /**
     * Performs the setShipmentNumber operation in this module.
     *
     * @param shipmentNumber the shipmentNumber input value
     */
    public void setShipmentNumber(String shipmentNumber) { this.shipmentNumber = shipmentNumber; }
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
     * Retrieves carrier id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCarrierId() { return carrierId; }
    /**
     * Performs the setCarrierId operation in this module.
     *
     * @param carrierId the carrierId input value
     */
    public void setCarrierId(Long carrierId) { this.carrierId = carrierId; }
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
     * Retrieves ship method data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getShipMethod() { return shipMethod; }
    /**
     * Performs the setShipMethod operation in this module.
     *
     * @param shipMethod the shipMethod input value
     */
    public void setShipMethod(String shipMethod) { this.shipMethod = shipMethod; }
    /**
     * Retrieves tracking number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTrackingNumber() { return trackingNumber; }
    /**
     * Performs the setTrackingNumber operation in this module.
     *
     * @param trackingNumber the trackingNumber input value
     */
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }
    /**
     * Retrieves pro number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getProNumber() { return proNumber; }
    /**
     * Performs the setProNumber operation in this module.
     *
     * @param proNumber the proNumber input value
     */
    public void setProNumber(String proNumber) { this.proNumber = proNumber; }
    /**
     * Retrieves estimated delivery data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getEstimatedDelivery() { return estimatedDelivery; }
    /**
     * Performs the setEstimatedDelivery operation in this module.
     *
     * @param estimatedDelivery the estimatedDelivery input value
     */
    public void setEstimatedDelivery(LocalDate estimatedDelivery) { this.estimatedDelivery = estimatedDelivery; }
    /**
     * Retrieves actual delivery data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getActualDelivery() { return actualDelivery; }
    /**
     * Performs the setActualDelivery operation in this module.
     *
     * @param actualDelivery the actualDelivery input value
     */
    public void setActualDelivery(LocalDateTime actualDelivery) { this.actualDelivery = actualDelivery; }
    /**
     * Retrieves total weight kg data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getTotalWeightKg() { return totalWeightKg; }
    /**
     * Performs the setTotalWeightKg operation in this module.
     *
     * @param totalWeightKg the totalWeightKg input value
     */
    public void setTotalWeightKg(BigDecimal totalWeightKg) { this.totalWeightKg = totalWeightKg; }
    /**
     * Retrieves total volume m3 data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getTotalVolumeM3() { return totalVolumeM3; }
    /**
     * Performs the setTotalVolumeM3 operation in this module.
     *
     * @param totalVolumeM3 the totalVolumeM3 input value
     */
    public void setTotalVolumeM3(BigDecimal totalVolumeM3) { this.totalVolumeM3 = totalVolumeM3; }
    /**
     * Retrieves freight cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getFreightCost() { return freightCost; }
    /**
     * Performs the setFreightCost operation in this module.
     *
     * @param freightCost the freightCost input value
     */
    public void setFreightCost(BigDecimal freightCost) { this.freightCost = freightCost; }
    /**
     * Retrieves freight currency data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getFreightCurrency() { return freightCurrency; }
    /**
     * Performs the setFreightCurrency operation in this module.
     *
     * @param freightCurrency the freightCurrency input value
     */
    public void setFreightCurrency(String freightCurrency) { this.freightCurrency = freightCurrency; }
    /**
     * Retrieves pod reference data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPodReference() { return podReference; }
    /**
     * Performs the setPodReference operation in this module.
     *
     * @param podReference the podReference input value
     */
    public void setPodReference(String podReference) { this.podReference = podReference; }
    /**
     * Retrieves pod signed by data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPodSignedBy() { return podSignedBy; }
    /**
     * Performs the setPodSignedBy operation in this module.
     *
     * @param podSignedBy the podSignedBy input value
     */
    public void setPodSignedBy(String podSignedBy) { this.podSignedBy = podSignedBy; }
    /**
     * Retrieves pod at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getPodAt() { return podAt; }
    /**
     * Performs the setPodAt operation in this module.
     *
     * @param podAt the podAt input value
     */
    public void setPodAt(LocalDateTime podAt) { this.podAt = podAt; }
    /**
     * Retrieves dock door id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getDockDoorId() { return dockDoorId; }
    /**
     * Performs the setDockDoorId operation in this module.
     *
     * @param dockDoorId the dockDoorId input value
     */
    public void setDockDoorId(Long dockDoorId) { this.dockDoorId = dockDoorId; }
    /**
     * Retrieves dispatched by data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getDispatchedBy() { return dispatchedBy; }
    /**
     * Performs the setDispatchedBy operation in this module.
     *
     * @param dispatchedBy the dispatchedBy input value
     */
    public void setDispatchedBy(Long dispatchedBy) { this.dispatchedBy = dispatchedBy; }
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
    /**
     * Retrieves lines data from the database.
     *
     * @throws ResourceNotFoundException if the entity is not found
     */
    public java.util.List<ShipmentLine> getLines() { return lines; }
    /**
     * Performs the setLines operation in this module.
     *
     * @param lines the lines input value
     */
    public void setLines(java.util.List<ShipmentLine> lines) { this.lines = lines; }
}