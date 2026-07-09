/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.entity
 * File              : AdvanceShippingNotice.java
 * Purpose           : JPA Entity representing a persistent database record in Wms Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AdvanceShippingNoticeController
 * Related Service   : AdvanceShippingNoticeService, AdvanceShippingNoticeServiceImpl
 * Related Repository: AdvanceShippingNoticeRepository
 * Related Entity    : AdvanceShippingNotice
 * Related DTO       : N/A
 * Related Mapper    : AdvanceShippingNoticeMapper
 * Related DB Table  : advance_shipping_notices
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : AdvanceShippingNoticeRepository, AdvanceShippingNoticeMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'advance_shipping_notices'. Defines persistent domain object for Wms Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.wms.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "advance_shipping_notices",
       uniqueConstraints = @UniqueConstraint(columnNames = {"company_id", "asn_number"}))
/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code AdvanceShippingNotice}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'advance_shipping_notices'.</p>
 *
 * <p><b>Database Table   :</b> {@code advance_shipping_notices}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public class AdvanceShippingNotice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    @Column(name = "asn_number", nullable = false, length = 50)
    private String asnNumber;

    @Column(name = "supplier_id")
    private Long supplierId;

    @Column(name = "carrier_id")
    private Long carrierId;

    @Column(nullable = false, length = 30)
    private String status = "PENDING";
    // PENDING, IN_TRANSIT, ARRIVED, RECEIVING, PARTIALLY_RECEIVED, RECEIVED, CANCELLED

    @Column(name = "expected_arrival")
    private LocalDate expectedArrival;

    @Column(name = "actual_arrival")
    private LocalDateTime actualArrival;

    @Column(name = "tracking_number", length = 100)
    private String trackingNumber;

    @Column(name = "po_reference", length = 100)
    private String poReference;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "dock_door_id")
    private Long dockDoorId;

    @Column(name = "checkin_id")
    private Long checkinId;

    @Column(name = "received_by")
    private Long receivedBy;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "asn", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AsnLine> lines = new ArrayList<>();

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
     * Retrieves asn number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getAsnNumber() { return asnNumber; }
    /**
     * Performs the setAsnNumber operation in this module.
     *
     * @param asnNumber the asnNumber input value
     */
    public void setAsnNumber(String asnNumber) { this.asnNumber = asnNumber; }
    /**
     * Retrieves supplier id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getSupplierId() { return supplierId; }
    /**
     * Performs the setSupplierId operation in this module.
     *
     * @param supplierId the supplierId input value
     */
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }
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
     * Retrieves expected arrival data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getExpectedArrival() { return expectedArrival; }
    /**
     * Performs the setExpectedArrival operation in this module.
     *
     * @param expectedArrival the expectedArrival input value
     */
    public void setExpectedArrival(LocalDate expectedArrival) { this.expectedArrival = expectedArrival; }
    /**
     * Retrieves actual arrival data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getActualArrival() { return actualArrival; }
    /**
     * Performs the setActualArrival operation in this module.
     *
     * @param actualArrival the actualArrival input value
     */
    public void setActualArrival(LocalDateTime actualArrival) { this.actualArrival = actualArrival; }
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
     * Retrieves po reference data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPoReference() { return poReference; }
    /**
     * Performs the setPoReference operation in this module.
     *
     * @param poReference the poReference input value
     */
    public void setPoReference(String poReference) { this.poReference = poReference; }
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
     * Retrieves checkin id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCheckinId() { return checkinId; }
    /**
     * Performs the setCheckinId operation in this module.
     *
     * @param checkinId the checkinId input value
     */
    public void setCheckinId(Long checkinId) { this.checkinId = checkinId; }
    /**
     * Retrieves received by data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getReceivedBy() { return receivedBy; }
    /**
     * Performs the setReceivedBy operation in this module.
     *
     * @param receivedBy the receivedBy input value
     */
    public void setReceivedBy(Long receivedBy) { this.receivedBy = receivedBy; }
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
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    public List<AsnLine> getLines() { return lines; }
    /**
     * Performs the setLines operation in this module.
     *
     * @param lines the lines input value
     */
    public void setLines(List<AsnLine> lines) { this.lines = lines; }
}