package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@Table(name = "platform_shipping_lane")
public class PlatformShippingLane {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "source_node_id", nullable = false)
    @NotNull
    private Long sourceNodeId;

    @Column(name = "destination_node_id", nullable = false)
    @NotNull
    private Long destinationNodeId;

    @Column(name = "distance_km", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal distanceKm;

    @Column(name = "expected_duration_minutes", nullable = false)
    @NotNull
    private Integer expectedDurationMinutes;

    @Column(name = "transport_mode", nullable = false)
    @NotNull
    @Size(max = 50)
    private String transportMode;

    @Column(name = "cost_factor", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal costFactor = BigDecimal.valueOf(1.00);

    @Column(name = "carbon_factor", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal carbonFactor = BigDecimal.valueOf(1.00);

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "ACTIVE";

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public Long getSourceNodeId() { return sourceNodeId; }
    public void setSourceNodeId(Long sourceNodeId) { this.sourceNodeId = sourceNodeId; }
    public Long getDestinationNodeId() { return destinationNodeId; }
    public void setDestinationNodeId(Long destinationNodeId) { this.destinationNodeId = destinationNodeId; }
    public BigDecimal getDistanceKm() { return distanceKm; }
    public void setDistanceKm(BigDecimal distanceKm) { this.distanceKm = distanceKm; }
    public Integer getExpectedDurationMinutes() { return expectedDurationMinutes; }
    public void setExpectedDurationMinutes(Integer expectedDurationMinutes) { this.expectedDurationMinutes = expectedDurationMinutes; }
    public String getTransportMode() { return transportMode; }
    public void setTransportMode(String transportMode) { this.transportMode = transportMode; }
    public BigDecimal getCostFactor() { return costFactor; }
    public void setCostFactor(BigDecimal costFactor) { this.costFactor = costFactor; }
    public BigDecimal getCarbonFactor() { return carbonFactor; }
    public void setCarbonFactor(BigDecimal carbonFactor) { this.carbonFactor = carbonFactor; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}