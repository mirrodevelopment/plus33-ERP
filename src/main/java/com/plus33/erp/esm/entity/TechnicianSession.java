package com.plus33.erp.esm.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "esm_technician_sessions")
public class TechnicianSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "technician_id", nullable = false)
    private Long technicianId;

    @Column(name = "device_id", length = 100)
    private String deviceId;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(name = "last_gps_latitude")
    private BigDecimal lastGpsLatitude;

    @Column(name = "last_gps_longitude")
    private BigDecimal lastGpsLongitude;

    @Column(name = "logged_in_at", nullable = false, updatable = false)
    private LocalDateTime loggedInAt = LocalDateTime.now();

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getTechnicianId() { return technicianId; }
    public void setTechnicianId(Long technicianId) { this.technicianId = technicianId; }
    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    public BigDecimal getLastGpsLatitude() { return lastGpsLatitude; }
    public void setLastGpsLatitude(BigDecimal lastGpsLatitude) { this.lastGpsLatitude = lastGpsLatitude; }
    public BigDecimal getLastGpsLongitude() { return lastGpsLongitude; }
    public void setLastGpsLongitude(BigDecimal lastGpsLongitude) { this.lastGpsLongitude = lastGpsLongitude; }
    public LocalDateTime getLoggedInAt() { return loggedInAt; }
}
