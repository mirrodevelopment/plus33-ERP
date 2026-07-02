package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_dispatch_assignment")
public class PlatformDispatchAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dispatch_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String dispatchCode;

    @Column(name = "vehicle_id", nullable = false)
    @NotNull
    private Long vehicleId;

    @Column(name = "driver_id", nullable = false)
    @NotNull
    private Long driverId;

    @Column(name = "route_id", nullable = false)
    @NotNull
    private Long routeId;

    @Column(name = "shipment_id", nullable = false)
    @NotNull
    private Long shipmentId;

    @Column(name = "assignment_status", nullable = false)
    @NotNull
    @Size(max = 50)
    private String assignmentStatus; // CREATED, ASSIGNED, ACCEPTED, EN_ROUTE, DELIVERED, FAILED

    @Column(name = "assigned_time", nullable = false)
    @NotNull
    private LocalDateTime assignedTime;

    @Column(name = "accepted_time")
    private LocalDateTime acceptedTime;

    @Column(name = "completed_time")
    private LocalDateTime completedTime;

    @Column(name = "estimated_eta", nullable = false)
    @NotNull
    private LocalDateTime estimatedEta;

    @Column(name = "actual_eta")
    private LocalDateTime actual_eta;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDispatchCode() { return dispatchCode; }
    public void setDispatchCode(String dispatchCode) { this.dispatchCode = dispatchCode; }
    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }
    public Long getDriverId() { return driverId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }
    public Long getRouteId() { return routeId; }
    public void setRouteId(Long routeId) { this.routeId = routeId; }
    public Long getShipmentId() { return shipmentId; }
    public void setShipmentId(Long shipmentId) { this.shipmentId = shipmentId; }
    public String getAssignmentStatus() { return assignmentStatus; }
    public void setAssignmentStatus(String assignmentStatus) { this.assignmentStatus = assignmentStatus; }
    public LocalDateTime getAssignedTime() { return assignedTime; }
    public void setAssignedTime(LocalDateTime assignedTime) { this.assignedTime = assignedTime; }
    public LocalDateTime getAcceptedTime() { return acceptedTime; }
    public void setAcceptedTime(LocalDateTime acceptedTime) { this.acceptedTime = acceptedTime; }
    public LocalDateTime getCompletedTime() { return completedTime; }
    public void setCompletedTime(LocalDateTime completedTime) { this.completedTime = completedTime; }
    public LocalDateTime getEstimatedEta() { return estimatedEta; }
    public void setEstimatedEta(LocalDateTime estimatedEta) { this.estimatedEta = estimatedEta; }
    public LocalDateTime getActualEta() { return actual_eta; }
    public void setActualEta(LocalDateTime actualEta) { this.actual_eta = actualEta; }
}