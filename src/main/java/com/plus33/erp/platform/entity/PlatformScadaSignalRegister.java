/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformScadaSignalRegister.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformScadaSignalRegisterController
 * Related Service   : PlatformScadaSignalRegisterService, PlatformScadaSignalRegisterServiceImpl
 * Related Repository: PlatformScadaSignalRegisterRepository
 * Related Entity    : PlatformScadaSignalRegister
 * Related DTO       : N/A
 * Related Mapper    : PlatformScadaSignalRegisterMapper
 * Related DB Table  : platform_scada_signal_register
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformScadaSignalRegisterRepository, PlatformScadaSignalRegisterMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_scada_signal_register'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformScadaSignalRegister}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_scada_signal_register'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_scada_signal_register}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_scada_signal_register")
public class PlatformScadaSignalRegister {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "device_id", nullable = false)
    @NotNull
    private Long deviceId;

    @Column(name = "register_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String registerCode;

    @Column(name = "register_type", nullable = false)
    @NotNull
    @Size(max = 50)
    private String registerType; // Holding Register, Input Register, Discrete Input, Coil

    @Column(name = "scaling_factor", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal scalingFactor = BigDecimal.valueOf(1.00);

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
     * Retrieves register code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRegisterCode() { return registerCode; }
    /**
     * Performs the setRegisterCode operation in this module.
     *
     * @param registerCode the registerCode input value
     */
    public void setRegisterCode(String registerCode) { this.registerCode = registerCode; }
    /**
     * Retrieves register type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRegisterType() { return registerType; }
    /**
     * Performs the setRegisterType operation in this module.
     *
     * @param registerType the registerType input value
     */
    public void setRegisterType(String registerType) { this.registerType = registerType; }
    /**
     * Retrieves scaling factor data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getScalingFactor() { return scalingFactor; }
    /**
     * Performs the setScalingFactor operation in this module.
     *
     * @param scalingFactor the scalingFactor input value
     */
    public void setScalingFactor(BigDecimal scalingFactor) { this.scalingFactor = scalingFactor; }
}