/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.entity
 * File              : WarehouseEdge.java
 * Purpose           : JPA Entity representing a persistent database record in Wms Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WarehouseEdgeController
 * Related Service   : WarehouseEdgeService, WarehouseEdgeServiceImpl
 * Related Repository: WarehouseEdgeRepository
 * Related Entity    : WarehouseEdge
 * Related DTO       : N/A
 * Related Mapper    : WarehouseEdgeMapper
 * Related DB Table  : warehouse_edges
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : WarehouseEdgeRepository, WarehouseEdgeMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'warehouse_edges'. Defines persistent domain object for Wms Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.wms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code WarehouseEdge}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'warehouse_edges'.</p>
 *
 * <p><b>Database Table   :</b> {@code warehouse_edges}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "warehouse_edges")
public class WarehouseEdge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_node_id", nullable = false)
    private WarehouseNode fromNode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_node_id", nullable = false)
    private WarehouseNode toNode;

    @Column(name = "distance_meters", nullable = false, precision = 10, scale = 2)
    private BigDecimal distanceMeters;

    @Column(nullable = false)
    private boolean bidirectional = true;

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
     * Retrieves from node data from the database.
     *
     * @return the WarehouseNode result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public WarehouseNode getFromNode() { return fromNode; }
    /**
     * Performs the setFromNode operation in this module.
     *
     * @param fromNode the fromNode input value
     */
    public void setFromNode(WarehouseNode fromNode) { this.fromNode = fromNode; }
    /**
     * Retrieves to node data from the database.
     *
     * @return the WarehouseNode result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public WarehouseNode getToNode() { return toNode; }
    /**
     * Performs the setToNode operation in this module.
     *
     * @param toNode the toNode input value
     */
    public void setToNode(WarehouseNode toNode) { this.toNode = toNode; }
    /**
     * Retrieves distance meters data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getDistanceMeters() { return distanceMeters; }
    /**
     * Performs the setDistanceMeters operation in this module.
     *
     * @param distanceMeters the distanceMeters input value
     */
    public void setDistanceMeters(BigDecimal distanceMeters) { this.distanceMeters = distanceMeters; }
    /**
     * Performs the isBidirectional operation in this module.
     *
     * @return true if operation succeeded, false otherwise
     */
    public boolean isBidirectional() { return bidirectional; }
    /**
     * Performs the setBidirectional operation in this module.
     *
     * @param bidirectional the bidirectional input value
     */
    public void setBidirectional(boolean bidirectional) { this.bidirectional = bidirectional; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
}