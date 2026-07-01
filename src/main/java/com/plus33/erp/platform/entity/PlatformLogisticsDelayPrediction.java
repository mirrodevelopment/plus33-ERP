package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_logistics_delay_prediction")
public class PlatformLogisticsDelayPrediction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transit_route_id", nullable = false)
    @NotNull
    private Long transitRouteId;

    @Column(name = "prediction_model", nullable = false)
    @NotNull
    @Size(max = 100)
    private String predictionModel;

    @Column(name = "prediction_confidence", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal predictionConfidence;

    @Column(name = "predicted_arrival", nullable = false)
    @NotNull
    private LocalDateTime predictedArrival;

    @Column(name = "actual_arrival")
    private LocalDateTime actualArrival;

    @Column(name = "generated_at", nullable = false)
    @NotNull
    private LocalDateTime generatedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getTransitRouteId() { return transitRouteId; }
    public void setTransitRouteId(Long transitRouteId) { this.transitRouteId = transitRouteId; }
    public String getPredictionModel() { return predictionModel; }
    public void setPredictionModel(String predictionModel) { this.predictionModel = predictionModel; }
    public BigDecimal getPredictionConfidence() { return predictionConfidence; }
    public void setPredictionConfidence(BigDecimal predictionConfidence) { this.predictionConfidence = predictionConfidence; }
    public LocalDateTime getPredictedArrival() { return predictedArrival; }
    public void setPredictedArrival(LocalDateTime predictedArrival) { this.predictedArrival = predictedArrival; }
    public LocalDateTime getActualArrival() { return actualArrival; }
    public void setActualArrival(LocalDateTime actualArrival) { this.actualArrival = actualArrival; }
    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }
}