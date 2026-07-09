/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.entity
 * File              : AsnLine.java
 * Purpose           : JPA Entity representing a persistent database record in Wms Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AsnLineController
 * Related Service   : AsnLineService, AsnLineServiceImpl
 * Related Repository: AsnLineRepository
 * Related Entity    : AsnLine
 * Related DTO       : N/A
 * Related Mapper    : AsnLineMapper
 * Related DB Table  : asn_lines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : AsnLineRepository, AsnLineMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'asn_lines'. Defines persistent domain object for Wms Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.wms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "asn_lines",
       uniqueConstraints = @UniqueConstraint(columnNames = {"asn_id", "line_number"}))
/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code AsnLine}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'asn_lines'.</p>
 *
 * <p><b>Database Table   :</b> {@code asn_lines}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public class AsnLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asn_id", nullable = false)
    private AdvanceShippingNotice asn;

    @Column(name = "line_number", nullable = false)
    private int lineNumber;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "lot_number", length = 50)
    private String lotNumber;

    @Column(name = "serial_number", length = 100)
    private String serialNumber;

    @Column(name = "expected_qty", nullable = false, precision = 18, scale = 6)
    private BigDecimal expectedQty;

    @Column(name = "received_qty", nullable = false, precision = 18, scale = 6)
    private BigDecimal receivedQty = BigDecimal.ZERO;

    @Column(name = "unit_id", nullable = false)
    private Long unitId;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "manufacture_date")
    private LocalDate manufactureDate;

    @Column(name = "unit_cost", precision = 18, scale = 6)
    private BigDecimal unitCost;

    @Column(nullable = false, length = 30)
    private String status = "PENDING";
    // PENDING, PARTIALLY_RECEIVED, RECEIVED, OVERAGE, CANCELLED

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
     * Retrieves asn data from the database.
     *
     * @return the AdvanceShippingNotice result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public AdvanceShippingNotice getAsn() { return asn; }
    /**
     * Performs the setAsn operation in this module.
     *
     * @param asn the asn input value
     */
    public void setAsn(AdvanceShippingNotice asn) { this.asn = asn; }
    /**
     * Retrieves line number data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public int getLineNumber() { return lineNumber; }
    /**
     * Performs the setLineNumber operation in this module.
     *
     * @param lineNumber the lineNumber input value
     */
    public void setLineNumber(int lineNumber) { this.lineNumber = lineNumber; }
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
     * Retrieves expected qty data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getExpectedQty() { return expectedQty; }
    /**
     * Performs the setExpectedQty operation in this module.
     *
     * @param expectedQty the expectedQty input value
     */
    public void setExpectedQty(BigDecimal expectedQty) { this.expectedQty = expectedQty; }
    /**
     * Retrieves received qty data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getReceivedQty() { return receivedQty; }
    /**
     * Performs the setReceivedQty operation in this module.
     *
     * @param receivedQty the receivedQty input value
     */
    public void setReceivedQty(BigDecimal receivedQty) { this.receivedQty = receivedQty; }
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
     * Retrieves expiry date data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getExpiryDate() { return expiryDate; }
    /**
     * Performs the setExpiryDate operation in this module.
     *
     * @param expiryDate the expiryDate input value
     */
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
    /**
     * Retrieves manufacture date data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getManufactureDate() { return manufactureDate; }
    /**
     * Performs the setManufactureDate operation in this module.
     *
     * @param manufactureDate the manufactureDate input value
     */
    public void setManufactureDate(LocalDate manufactureDate) { this.manufactureDate = manufactureDate; }
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