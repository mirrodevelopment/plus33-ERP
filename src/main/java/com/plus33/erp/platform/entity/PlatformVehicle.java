package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@Table(name = "platform_vehicle")
public class PlatformVehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "vehicle_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String vehicleCode;

    @Column(name = "capacity_kg", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal capacityKg;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "AVAILABLE";

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getVehicleCode() { return vehicleCode; }
    public void setVehicleCode(String vehicleCode) { this.vehicleCode = vehicleCode; }
    public BigDecimal getCapacityKg() { return capacityKg; }
    public void setCapacityKg(BigDecimal capacityKg) { this.capacityKg = capacityKg; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}