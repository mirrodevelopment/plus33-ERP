/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformMachineryTelemetry.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformMachineryTelemetryController
 * Related Service   : PlatformMachineryTelemetryService, PlatformMachineryTelemetryServiceImpl
 * Related Repository: PlatformMachineryTelemetryRepository
 * Related Entity    : PlatformMachineryTelemetry
 * Related DTO       : N/A
 * Related Mapper    : PlatformMachineryTelemetryMapper
 * Related DB Table  : platform_machinery_telemetry
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformMachineryTelemetryRepository, PlatformMachineryTelemetryMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_machinery_telemetry'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
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
 * <p><b>Class  :</b> {@code PlatformMachineryTelemetry}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_machinery_telemetry'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_machinery_telemetry}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_machinery_telemetry")
public class PlatformMachineryTelemetry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_id", nullable = false)
    @NotNull
    private Long deviceId;

    @Column(name = "signal_id", nullable = false)
    @NotNull
    private Long signalId;

    @Column(name = "recorded_at", nullable = false)
    @NotNull
    private LocalDateTime recordedAt;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String quality;

    @Column(nullable = false, precision = 19, scale = 4)
    @NotNull
    private BigDecimal value;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String unit;

    @Column(name = "sequence_num", nullable = false)
    @NotNull
    private Long sequenceNum;

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
     * Retrieves device id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getDeviceId() { return deviceId; }
    /**
     * Performs the setDeviceId operation in this module.
     *
     * @param deviceId the deviceId input value
     */
    public void setDeviceId(Long deviceId) { this.deviceId = deviceId; }
    /**
     * Retrieves signal id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getSignalId() { return signalId; }
    /**
     * Performs the setSignalId operation in this module.
     *
     * @param signalId the signalId input value
     */
    public void setSignalId(Long signalId) { this.signalId = signalId; }
    /**
     * Retrieves recorded at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getRecordedAt() { return recordedAt; }
    /**
     * Performs the setRecordedAt operation in this module.
     *
     * @param recordedAt the recordedAt input value
     */
    public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }
    /**
     * Retrieves quality data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getQuality() { return quality; }
    /**
     * Performs the setQuality operation in this module.
     *
     * @param quality the quality input value
     */
    public void setQuality(String quality) { this.quality = quality; }
    /**
     * Retrieves value data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getValue() { return value; }
    /**
     * Performs the setValue operation in this module.
     *
     * @param value the value input value
     */
    public void setValue(BigDecimal value) { this.value = value; }
    /**
     * Retrieves unit data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getUnit() { return unit; }
    /**
     * Performs the setUnit operation in this module.
     *
     * @param unit the unit input value
     */
    public void setUnit(String unit) { this.unit = unit; }
    /**
     * Retrieves sequence num data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getSequenceNum() { return sequenceNum; }
    /**
     * Performs the setSequenceNum operation in this module.
     *
     * @param sequenceNum the sequenceNum input value
     */
    public void setSequenceNum(Long sequenceNum) { this.sequenceNum = sequenceNum; }
}