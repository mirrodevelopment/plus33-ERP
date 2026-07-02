package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_routing_optimization_recommendation")
public class PlatformRoutingOptimizationRecommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recommended_route", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String recommendedRoute;

    @Column(name = "estimated_savings_cost", nullable = false, precision = 12, scale = 2)
    @NotNull
    private BigDecimal estimatedSavingsCost;

    @Column(name = "estimated_time_saved_m", nullable = false)
    @NotNull
    private Integer estimatedTimeSavedM;

    @Column(name = "estimated_fuel_saved_l", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal estimatedFuelSavedL;

    @Column(name = "estimated_co2_saved_kg", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal estimatedCo2SavedKg;

    @Column(name = "confidence_score", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal confidenceScore;

    @Column(name = "algorithm_version", nullable = false)
    @NotNull
    @Size(max = 50)
    private String algorithmVersion;

    @Column(nullable = false)
    @NotNull
    private Boolean accepted = false;

    @Column(nullable = false)
    @NotNull
    private Boolean implemented = false;

    @Column(name = "created_at", nullable = false)
    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRecommendedRoute() { return recommendedRoute; }
    public void setRecommendedRoute(String recommendedRoute) { this.recommendedRoute = recommendedRoute; }
    public BigDecimal getEstimatedSavingsCost() { return estimatedSavingsCost; }
    public void setEstimatedSavingsCost(BigDecimal estimatedSavingsCost) { this.estimatedSavingsCost = estimatedSavingsCost; }
    public Integer getEstimatedTimeSavedM() { return estimatedTimeSavedM; }
    public void setEstimatedTimeSavedM(Integer estimatedTimeSavedM) { this.estimatedTimeSavedM = estimatedTimeSavedM; }
    public BigDecimal getEstimatedFuelSavedL() { return estimatedFuelSavedL; }
    public void setEstimatedFuelSavedL(BigDecimal estimatedFuelSavedL) { this.estimatedFuelSavedL = estimatedFuelSavedL; }
    public BigDecimal getEstimatedCo2SavedKg() { return estimatedCo2SavedKg; }
    public void setEstimatedCo2SavedKg(BigDecimal estimatedCo2SavedKg) { this.estimatedCo2SavedKg = estimatedCo2SavedKg; }
    public BigDecimal getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(BigDecimal confidenceScore) { this.confidenceScore = confidenceScore; }
    public String getAlgorithmVersion() { return algorithmVersion; }
    public void setAlgorithmVersion(String algorithmVersion) { this.algorithmVersion = algorithmVersion; }
    public Boolean getAccepted() { return accepted; }
    public void setAccepted(Boolean accepted) { this.accepted = accepted; }
    public Boolean getImplemented() { return implemented; }
    public void setImplemented(Boolean implemented) { this.implemented = implemented; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}