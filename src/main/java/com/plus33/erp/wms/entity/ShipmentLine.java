/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.entity
 * File              : ShipmentLine.java
 * Purpose           : JPA Entity representing a persistent database record in Wms Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ShipmentLineController
 * Related Service   : ShipmentLineService, ShipmentLineServiceImpl
 * Related Repository: ShipmentLineRepository
 * Related Entity    : ShipmentLine
 * Related DTO       : N/A
 * Related Mapper    : ShipmentLineMapper
 * Related DB Table  : shipment_lines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ShipmentLineRepository, ShipmentLineMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'shipment_lines'. Defines persistent domain object for Wms Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.wms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code ShipmentLine}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'shipment_lines'.</p>
 *
 * <p><b>Database Table   :</b> {@code shipment_lines}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "shipment_lines")
public class ShipmentLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_id", nullable = false)
    private Shipment shipment;

    @Column(name = "picking_work_id")
    private Long pickingWorkId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "lot_number", length = 50)
    private String lotNumber;

    @Column(name = "serial_number", length = 100)
    private String serialNumber;

    @Column(name = "shipped_qty", nullable = false, precision = 18, scale = 6)
    private BigDecimal shippedQty;

    @Column(name = "unit_id", nullable = false)
    private Long unitId;

    @Column(name = "source_type", length = 30)
    private String sourceType;

    @Column(name = "source_id")
    private Long sourceId;

    @Column(name = "source_line_id")
    private Long sourceLineId;

    @Column(name = "package_number", length = 50)
    private String packageNumber;

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
     * Retrieves shipment data from the database.
     *
     * @return the Shipment result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Shipment getShipment() { return shipment; }
    /**
     * Performs the setShipment operation in this module.
     *
     * @param shipment the shipment input value
     */
    public void setShipment(Shipment shipment) { this.shipment = shipment; }
    /**
     * Retrieves picking work id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getPickingWorkId() { return pickingWorkId; }
    /**
     * Performs the setPickingWorkId operation in this module.
     *
     * @param pickingWorkId the pickingWorkId input value
     */
    public void setPickingWorkId(Long pickingWorkId) { this.pickingWorkId = pickingWorkId; }
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
     * Retrieves shipped qty data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getShippedQty() { return shippedQty; }
    /**
     * Performs the setShippedQty operation in this module.
     *
     * @param shippedQty the shippedQty input value
     */
    public void setShippedQty(BigDecimal shippedQty) { this.shippedQty = shippedQty; }
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
     * Retrieves package number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPackageNumber() { return packageNumber; }
    /**
     * Performs the setPackageNumber operation in this module.
     *
     * @param packageNumber the packageNumber input value
     */
    public void setPackageNumber(String packageNumber) { this.packageNumber = packageNumber; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
}