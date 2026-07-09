/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformGeofenceDefinition.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformGeofenceDefinitionController
 * Related Service   : PlatformGeofenceDefinitionService, PlatformGeofenceDefinitionServiceImpl
 * Related Repository: PlatformGeofenceDefinitionRepository
 * Related Entity    : PlatformGeofenceDefinition
 * Related DTO       : N/A
 * Related Mapper    : PlatformGeofenceDefinitionMapper
 * Related DB Table  : platform_geofence_definition
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformGeofenceDefinitionRepository, PlatformGeofenceDefinitionMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_geofence_definition'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformGeofenceDefinition}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_geofence_definition'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_geofence_definition}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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
     * Retrieves version data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getVersion() { return version; }
    /**
     * Performs the setVersion operation in this module.
     *
     * @param version the version input value
     */
    public void setVersion(Integer version) { this.version = version; }
    /**
     * Retrieves geofence code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getGeofenceCode() { return geofenceCode; }
    /**
     * Performs the setGeofenceCode operation in this module.
     *
     * @param geofenceCode the geofenceCode input value
     */
    public void setGeofenceCode(String geofenceCode) { this.geofenceCode = geofenceCode; }
    /**
     * Retrieves geofence type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getGeofenceType() { return geofenceType; }
    /**
     * Performs the setGeofenceType operation in this module.
     *
     * @param geofenceType the geofenceType input value
     */
    public void setGeofenceType(String geofenceType) { this.geofenceType = geofenceType; }
    /**
     * Retrieves geometry wkt data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getGeometryWkt() { return geometryWkt; }
    /**
     * Performs the setGeometryWkt operation in this module.
     *
     * @param geometryWkt the geometryWkt input value
     */
    public void setGeometryWkt(String geometryWkt) { this.geometryWkt = geometryWkt; }
    /**
     * Retrieves center lat data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getCenterLat() { return centerLat; }
    /**
     * Performs the setCenterLat operation in this module.
     *
     * @param centerLat the centerLat input value
     */
    public void setCenterLat(BigDecimal centerLat) { this.centerLat = centerLat; }
    /**
     * Retrieves center lng data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getCenterLng() { return centerLng; }
    /**
     * Performs the setCenterLng operation in this module.
     *
     * @param centerLng the centerLng input value
     */
    public void setCenterLng(BigDecimal centerLng) { this.centerLng = centerLng; }
    /**
     * Retrieves radius meters data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getRadiusMeters() { return radiusMeters; }
    /**
     * Performs the setRadiusMeters operation in this module.
     *
     * @param radiusMeters the radiusMeters input value
     */
    public void setRadiusMeters(BigDecimal radiusMeters) { this.radiusMeters = radiusMeters; }
    /**
     * Retrieves altitude min data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getAltitudeMin() { return altitudeMin; }
    /**
     * Performs the setAltitudeMin operation in this module.
     *
     * @param altitudeMin the altitudeMin input value
     */
    public void setAltitudeMin(BigDecimal altitudeMin) { this.altitudeMin = altitudeMin; }
    /**
     * Retrieves altitude max data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getAltitudeMax() { return altitudeMax; }
    /**
     * Performs the setAltitudeMax operation in this module.
     *
     * @param altitudeMax the altitudeMax input value
     */
    public void setAltitudeMax(BigDecimal altitudeMax) { this.altitudeMax = altitudeMax; }
    /**
     * Retrieves timezone data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTimezone() { return timezone; }
    /**
     * Performs the setTimezone operation in this module.
     *
     * @param timezone the timezone input value
     */
    public void setTimezone(String timezone) { this.timezone = timezone; }
    /**
     * Retrieves active from data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getActiveFrom() { return activeFrom; }
    /**
     * Performs the setActiveFrom operation in this module.
     *
     * @param activeFrom the activeFrom input value
     */
    public void setActiveFrom(LocalDateTime activeFrom) { this.activeFrom = activeFrom; }
    /**
     * Retrieves active until data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getActiveUntil() { return activeUntil; }
    /**
     * Performs the setActiveUntil operation in this module.
     *
     * @param activeUntil the activeUntil input value
     */
    public void setActiveUntil(LocalDateTime activeUntil) { this.activeUntil = activeUntil; }
    /**
     * Retrieves tenant id data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTenantId() { return tenantId; }
    /**
     * Performs the setTenantId operation in this module.
     *
     * @param tenantId the tenantId input value
     */
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
}