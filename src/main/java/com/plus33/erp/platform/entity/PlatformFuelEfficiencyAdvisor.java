package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_fuel_efficiency_advisor")
public class PlatformFuelEfficiencyAdvisor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recommendation_type", nullable = false)
    @NotNull
    @Size(max = 100)
    private String recommendationType; // EcoSpeedLimit, ReduceIdle, CruiseControl

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String priority;

    @Column(name = "expected_fuel_saving_l", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal expectedFuelSavingL;

    @Column(name = "expected_cost_saving", nullable = false, precision = 12, scale = 2)
    @NotNull
    private BigDecimal expectedCostSaving;

    @Column(name = "expected_emission_reduce", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal expectedEmissionReduce;

    @Column(name = "generated_by", nullable = false)
    @NotNull
    @Size(max = 100)
    private String generatedBy;

    @Column(nullable = false)
    @NotNull
    private Boolean acknowledged = false;

    @Column(name = "applied_at")
    private LocalDateTime appliedAt;

    @Column(name = "created_at", nullable = false)
    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRecommendationType() { return recommendationType; }
    public void setRecommendationType(String recommendationType) { this.recommendationType = recommendationType; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public BigDecimal getExpectedFuelSavingL() { return expectedFuelSavingL; }
    public void setExpectedFuelSavingL(BigDecimal expectedFuelSavingL) { this.expectedFuelSavingL = expectedFuelSavingL; }
    public BigDecimal getExpectedCostSaving() { return expectedCostSaving; }
    public void setExpectedCostSaving(BigDecimal expectedCostSaving) { this.expectedCostSaving = expectedCostSaving; }
    public BigDecimal getExpectedEmissionReduce() { return expectedEmissionReduce; }
    public void setExpectedEmissionReduce(BigDecimal expectedEmissionReduce) { this.expectedEmissionReduce = expectedEmissionReduce; }
    public String getGeneratedBy() { return generatedBy; }
    public void setGeneratedBy(String generatedBy) { this.generatedBy = generatedBy; }
    public Boolean getAcknowledged() { return acknowledged; }
    public void setAcknowledged(Boolean acknowledged) { this.acknowledged = acknowledged; }
    public LocalDateTime getAppliedAt() { return appliedAt; }
    public void setAppliedAt(LocalDateTime appliedAt) { this.appliedAt = appliedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}