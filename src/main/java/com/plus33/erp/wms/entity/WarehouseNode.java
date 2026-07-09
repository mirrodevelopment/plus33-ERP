/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.entity
 * File              : WarehouseNode.java
 * Purpose           : JPA Entity representing a persistent database record in Wms Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WarehouseNodeController
 * Related Service   : WarehouseNodeService, WarehouseNodeServiceImpl
 * Related Repository: WarehouseNodeRepository
 * Related Entity    : WarehouseNode
 * Related DTO       : N/A
 * Related Mapper    : WarehouseNodeMapper
 * Related DB Table  : warehouse_nodes
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : WarehouseNodeRepository, WarehouseNodeMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'warehouse_nodes'. Defines persistent domain object for Wms Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.wms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code WarehouseNode}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'warehouse_nodes'.</p>
 *
 * <p><b>Database Table   :</b> {@code warehouse_nodes}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "warehouse_nodes")
public class WarehouseNode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    @Column(name = "location_id", unique = true)
    private Long locationId;

    @Column(name = "node_code", nullable = false, length = 50)
    private String nodeCode;

    @Column(name = "x_coord", nullable = false, precision = 10, scale = 2)
    private BigDecimal xCoord = BigDecimal.ZERO;

    @Column(name = "y_coord", nullable = false, precision = 10, scale = 2)
    private BigDecimal yCoord = BigDecimal.ZERO;

    @Column(name = "z_coord", nullable = false, precision = 10, scale = 2)
    private BigDecimal zCoord = BigDecimal.ZERO;

    @Column(name = "temperature_class", length = 20)
    private String temperatureClass = "AMBIENT";

    @Column(name = "accessibility_flags", length = 100)
    private String accessibilityFlags;

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
     * Retrieves node code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getNodeCode() { return nodeCode; }
    /**
     * Performs the setNodeCode operation in this module.
     *
     * @param nodeCode the nodeCode input value
     */
    public void setNodeCode(String nodeCode) { this.nodeCode = nodeCode; }
    /**
     * Retrieves coord data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getxCoord() { return xCoord; }
    /**
     * Performs the setxCoord operation in this module.
     *
     * @param xCoord the xCoord input value
     */
    public void setxCoord(BigDecimal xCoord) { this.xCoord = xCoord; }
    /**
     * Retrieves coord data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getyCoord() { return yCoord; }
    /**
     * Performs the setyCoord operation in this module.
     *
     * @param yCoord the yCoord input value
     */
    public void setyCoord(BigDecimal yCoord) { this.yCoord = yCoord; }
    /**
     * Retrieves coord data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getzCoord() { return zCoord; }
    /**
     * Performs the setzCoord operation in this module.
     *
     * @param zCoord the zCoord input value
     */
    public void setzCoord(BigDecimal zCoord) { this.zCoord = zCoord; }
    /**
     * Retrieves temperature class data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTemperatureClass() { return temperatureClass; }
    /**
     * Performs the setTemperatureClass operation in this module.
     *
     * @param temperatureClass the temperatureClass input value
     */
    public void setTemperatureClass(String temperatureClass) { this.temperatureClass = temperatureClass; }
    /**
     * Retrieves accessibility flags data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getAccessibilityFlags() { return accessibilityFlags; }
    /**
     * Performs the setAccessibilityFlags operation in this module.
     *
     * @param accessibilityFlags the accessibilityFlags input value
     */
    public void setAccessibilityFlags(String accessibilityFlags) { this.accessibilityFlags = accessibilityFlags; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
}