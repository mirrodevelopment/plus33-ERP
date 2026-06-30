package com.plus33.erp.wms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }
    public Long getLocationId() { return locationId; }
    public void setLocationId(Long locationId) { this.locationId = locationId; }
    public String getNodeCode() { return nodeCode; }
    public void setNodeCode(String nodeCode) { this.nodeCode = nodeCode; }
    public BigDecimal getxCoord() { return xCoord; }
    public void setxCoord(BigDecimal xCoord) { this.xCoord = xCoord; }
    public BigDecimal getyCoord() { return yCoord; }
    public void setyCoord(BigDecimal yCoord) { this.yCoord = yCoord; }
    public BigDecimal getzCoord() { return zCoord; }
    public void setzCoord(BigDecimal zCoord) { this.zCoord = zCoord; }
    public String getTemperatureClass() { return temperatureClass; }
    public void setTemperatureClass(String temperatureClass) { this.temperatureClass = temperatureClass; }
    public String getAccessibilityFlags() { return accessibilityFlags; }
    public void setAccessibilityFlags(String accessibilityFlags) { this.accessibilityFlags = accessibilityFlags; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
