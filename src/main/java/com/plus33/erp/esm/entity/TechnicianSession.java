/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Esm Module
 * Package           : com.plus33.erp.esm.entity
 * File              : TechnicianSession.java
 * Purpose           : JPA Entity representing a persistent database record in Esm Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TechnicianSessionController
 * Related Service   : TechnicianSessionService, TechnicianSessionServiceImpl
 * Related Repository: TechnicianSessionRepository
 * Related Entity    : TechnicianSession
 * Related DTO       : N/A
 * Related Mapper    : TechnicianSessionMapper
 * Related DB Table  : esm_technician_sessions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : TechnicianSessionRepository, TechnicianSessionMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'esm_technician_sessions'. Defines persistent domain object for Esm Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.esm.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Esm Module</b>
 *
 * <p><b>Class  :</b> {@code TechnicianSession}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.esm.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'esm_technician_sessions'.</p>
 *
 * <p><b>Database Table   :</b> {@code esm_technician_sessions}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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
     * Retrieves technician id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getTechnicianId() { return technicianId; }
    /**
     * Performs the setTechnicianId operation in this module.
     *
     * @param technicianId the technicianId input value
     */
    public void setTechnicianId(Long technicianId) { this.technicianId = technicianId; }
    /**
     * Retrieves device id data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDeviceId() { return deviceId; }
    /**
     * Performs the setDeviceId operation in this module.
     *
     * @param deviceId the deviceId input value
     */
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    /**
     * Retrieves active data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getActive() { return active; }
    /**
     * Performs the setActive operation in this module.
     *
     * @param active the active input value
     */
    public void setActive(Boolean active) { this.active = active; }
    /**
     * Retrieves last gps latitude data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getLastGpsLatitude() { return lastGpsLatitude; }
    /**
     * Performs the setLastGpsLatitude operation in this module.
     *
     * @param lastGpsLatitude the lastGpsLatitude input value
     */
    public void setLastGpsLatitude(BigDecimal lastGpsLatitude) { this.lastGpsLatitude = lastGpsLatitude; }
    /**
     * Retrieves last gps longitude data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getLastGpsLongitude() { return lastGpsLongitude; }
    /**
     * Performs the setLastGpsLongitude operation in this module.
     *
     * @param lastGpsLongitude the lastGpsLongitude input value
     */
    public void setLastGpsLongitude(BigDecimal lastGpsLongitude) { this.lastGpsLongitude = lastGpsLongitude; }
    /**
     * Retrieves logged in at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getLoggedInAt() { return loggedInAt; }
}