package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_circuit_breaker_stats")
public class PlatformCircuitBreakerStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "breaker_name", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String breakerName;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "CLOSED";

    @Column(name = "failures_count", nullable = false)
    @NotNull
    private Integer failuresCount = 0;

    @Column(name = "success_ratio", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal successRatio = BigDecimal.valueOf(100.00);

    @Column(name = "recovery_time_sec", nullable = false)
    @NotNull
    private Integer recoveryTimeSec = 60;

    @Column(name = "last_trip_time")
    private LocalDateTime lastTripTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getBreakerName() { return breakerName; }
    public void setBreakerName(String breakerName) { this.breakerName = breakerName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getFailuresCount() { return failuresCount; }
    public void setFailuresCount(Integer failuresCount) { this.failuresCount = failuresCount; }
    public BigDecimal getSuccessRatio() { return successRatio; }
    public void setSuccessRatio(BigDecimal successRatio) { this.successRatio = successRatio; }
    public Integer getRecoveryTimeSec() { return recoveryTimeSec; }
    public void setRecoveryTimeSec(Integer recoveryTimeSec) { this.recoveryTimeSec = recoveryTimeSec; }
    public LocalDateTime getLastTripTime() { return lastTripTime; }
    public void setLastTripTime(LocalDateTime lastTripTime) { this.lastTripTime = lastTripTime; }
}