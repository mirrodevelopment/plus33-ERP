package com.plus33.erp.wms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }
    public WarehouseNode getFromNode() { return fromNode; }
    public void setFromNode(WarehouseNode fromNode) { this.fromNode = fromNode; }
    public WarehouseNode getToNode() { return toNode; }
    public void setToNode(WarehouseNode toNode) { this.toNode = toNode; }
    public BigDecimal getDistanceMeters() { return distanceMeters; }
    public void setDistanceMeters(BigDecimal distanceMeters) { this.distanceMeters = distanceMeters; }
    public boolean isBidirectional() { return bidirectional; }
    public void setBidirectional(boolean bidirectional) { this.bidirectional = bidirectional; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
