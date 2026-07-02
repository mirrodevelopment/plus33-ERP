package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getDeviceId() { return deviceId; }
    public void setDeviceId(Long deviceId) { this.deviceId = deviceId; }
    public Long getSignalId() { return signalId; }
    public void setSignalId(Long signalId) { this.signalId = signalId; }
    public LocalDateTime getRecordedAt() { return recordedAt; }
    public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }
    public String getQuality() { return quality; }
    public void setQuality(String quality) { this.quality = quality; }
    public BigDecimal getValue() { return value; }
    public void setValue(BigDecimal value) { this.value = value; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public Long getSequenceNum() { return sequenceNum; }
    public void setSequenceNum(Long sequenceNum) { this.sequenceNum = sequenceNum; }
}