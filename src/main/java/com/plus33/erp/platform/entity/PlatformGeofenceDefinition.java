package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_geofence_definition")
public class PlatformGeofenceDefinition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "geofence_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String geofenceCode;

    @Column(name = "geofence_type", nullable = false)
    @NotNull
    @Size(max = 50)
    private String geofenceType; // Polygon, Circle, Rectangle, Corridor

    @Column(name = "geometry_wkt", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String geometryWkt;

    @Column(name = "center_lat", nullable = false, precision = 9, scale = 6)
    @NotNull
    private BigDecimal centerLat;

    @Column(name = "center_lng", nullable = false, precision = 9, scale = 6)
    @NotNull
    private BigDecimal centerLng;

    @Column(name = "radius_meters", precision = 10, scale = 2)
    private BigDecimal radiusMeters;

    @Column(name = "altitude_min", precision = 7, scale = 2)
    private BigDecimal altitudeMin;

    @Column(name = "altitude_max", precision = 7, scale = 2)
    private BigDecimal altitudeMax;

    @Column(nullable = false)
    @NotNull
    @Size(max = 100)
    private String timezone;

    @Column(name = "active_from")
    private LocalDateTime activeFrom;

    @Column(name = "active_until")
    private LocalDateTime activeUntil;

    @Column(name = "tenant_id", nullable = false)
    @NotNull
    @Size(max = 100)
    private String tenantId = "DEFAULT_TENANT";

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getGeofenceCode() { return geofenceCode; }
    public void setGeofenceCode(String geofenceCode) { this.geofenceCode = geofenceCode; }
    public String getGeofenceType() { return geofenceType; }
    public void setGeofenceType(String geofenceType) { this.geofenceType = geofenceType; }
    public String getGeometryWkt() { return geometryWkt; }
    public void setGeometryWkt(String geometryWkt) { this.geometryWkt = geometryWkt; }
    public BigDecimal getCenterLat() { return centerLat; }
    public void setCenterLat(BigDecimal centerLat) { this.centerLat = centerLat; }
    public BigDecimal getCenterLng() { return centerLng; }
    public void setCenterLng(BigDecimal centerLng) { this.centerLng = centerLng; }
    public BigDecimal getRadiusMeters() { return radiusMeters; }
    public void setRadiusMeters(BigDecimal radiusMeters) { this.radiusMeters = radiusMeters; }
    public BigDecimal getAltitudeMin() { return altitudeMin; }
    public void setAltitudeMin(BigDecimal altitudeMin) { this.altitudeMin = altitudeMin; }
    public BigDecimal getAltitudeMax() { return altitudeMax; }
    public void setAltitudeMax(BigDecimal altitudeMax) { this.altitudeMax = altitudeMax; }
    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }
    public LocalDateTime getActiveFrom() { return activeFrom; }
    public void setActiveFrom(LocalDateTime activeFrom) { this.activeFrom = activeFrom; }
    public LocalDateTime getActiveUntil() { return activeUntil; }
    public void setActiveUntil(LocalDateTime activeUntil) { this.activeUntil = activeUntil; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
}