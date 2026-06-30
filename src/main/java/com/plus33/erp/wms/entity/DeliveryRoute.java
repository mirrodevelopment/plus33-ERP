package com.plus33.erp.wms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "delivery_routes")
public class DeliveryRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "carrier_id")
    private Long carrierId;

    @Column(name = "route_number", nullable = false, unique = true, length = 50)
    private String routeNumber;

    @Column(name = "driver_name", length = 100)
    private String driverName;

    @Column(name = "vehicle_number", length = 50)
    private String vehicleNumber;

    @Column(name = "max_capacity_kg", precision = 12, scale = 3)
    private BigDecimal maxCapacityKg;

    @Column(name = "current_load_kg", precision = 12, scale = 3)
    private BigDecimal currentLoadKg = BigDecimal.ZERO;

    @Column(nullable = false, length = 30)
    private String status = "PLANNED";

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public Long getCarrierId() { return carrierId; }
    public void setCarrierId(Long carrierId) { this.carrierId = carrierId; }
    public String getRouteNumber() { return routeNumber; }
    public void setRouteNumber(String routeNumber) { this.routeNumber = routeNumber; }
    public String getDriverName() { return driverName; }
    public void setDriverName(String driverName) { this.driverName = driverName; }
    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }
    public BigDecimal getMaxCapacityKg() { return maxCapacityKg; }
    public void setMaxCapacityKg(BigDecimal maxCapacityKg) { this.maxCapacityKg = maxCapacityKg; }
    public BigDecimal getCurrentLoadKg() { return currentLoadKg; }
    public void setCurrentLoadKg(BigDecimal currentLoadKg) { this.currentLoadKg = currentLoadKg; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
