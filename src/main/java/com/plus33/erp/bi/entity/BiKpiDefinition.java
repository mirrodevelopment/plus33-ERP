package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bi_kpi_definition")
public class BiKpiDefinition {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "kpi_code", nullable = false, unique = true, length = 100) private String kpiCode;
    @Column(name = "kpi_name", nullable = false, length = 200) private String kpiName;
    @Column(name = "kpi_category", length = 100) private String kpiCategory;
    @Column(columnDefinition = "TEXT") private String description;
    @Column(length = 50) private String unit;
    @Column(nullable = false, length = 10) private String direction = "HIGHER";
    @Column(name = "target_value", precision = 19, scale = 4) private BigDecimal targetValue;
    @Column(name = "threshold_warning", precision = 19, scale = 4) private BigDecimal thresholdWarning;
    @Column(name = "threshold_critical", precision = 19, scale = 4) private BigDecimal thresholdCritical;
    @Column(nullable = false, length = 30) private String status = "DRAFT";
    @Column(length = 100) private String owner;
    @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt = LocalDateTime.now();
    @Column(name = "updated_at", nullable = false) private LocalDateTime updatedAt = LocalDateTime.now();

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getKpiCode() { return kpiCode; } public void setKpiCode(String v) { this.kpiCode = v; }
    public String getKpiName() { return kpiName; } public void setKpiName(String v) { this.kpiName = v; }
    public String getKpiCategory() { return kpiCategory; } public void setKpiCategory(String v) { this.kpiCategory = v; }
    public String getDescription() { return description; } public void setDescription(String v) { this.description = v; }
    public String getUnit() { return unit; } public void setUnit(String v) { this.unit = v; }
    public String getDirection() { return direction; } public void setDirection(String v) { this.direction = v; }
    public BigDecimal getTargetValue() { return targetValue; } public void setTargetValue(BigDecimal v) { this.targetValue = v; }
    public BigDecimal getThresholdWarning() { return thresholdWarning; } public void setThresholdWarning(BigDecimal v) { this.thresholdWarning = v; }
    public BigDecimal getThresholdCritical() { return thresholdCritical; } public void setThresholdCritical(BigDecimal v) { this.thresholdCritical = v; }
    public String getStatus() { return status; } public void setStatus(String v) { this.status = v; }
    public String getOwner() { return owner; } public void setOwner(String v) { this.owner = v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; } public void setUpdatedAt(LocalDateTime v) { this.updatedAt = v; }
}
