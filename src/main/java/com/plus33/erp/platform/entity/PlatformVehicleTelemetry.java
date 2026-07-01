package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_vehicle_telemetry")
public class PlatformVehicleTelemetry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vehicle_id", nullable = false)
    @NotNull
    private Long vehicleId;

    @Column(nullable = false, precision = 9, scale = 6)
    @NotNull
    private BigDecimal latitude;

    @Column(nullable = false, precision = 9, scale = 6)
    @NotNull
    private BigDecimal longitude;

    @Column(name = "speed_kmh", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal speedKmh;

    @Column(name = "recorded_at", nullable = false)
    @NotNull
    private LocalDateTime recordedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }
    public BigDecimal getLatitude() { return latitude; }
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }
    public BigDecimal getLongitude() { return longitude; }
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }
    public BigDecimal getSpeedKmh() { return speedKmh; }
    public void setSpeedKmh(BigDecimal speedKmh) { this.speedKmh = speedKmh; }
    public LocalDateTime getRecordedAt() { return recordedAt; }
    public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }
}