package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public Long getDeviceId() { return deviceId; }
    public void setDeviceId(Long deviceId) { this.deviceId = deviceId; }
    public String getRegisterCode() { return registerCode; }
    public void setRegisterCode(String registerCode) { this.registerCode = registerCode; }
    public String getRegisterType() { return registerType; }
    public void setRegisterType(String registerType) { this.registerType = registerType; }
    public BigDecimal getScalingFactor() { return scalingFactor; }
    public void setScalingFactor(BigDecimal scalingFactor) { this.scalingFactor = scalingFactor; }
}