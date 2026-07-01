package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_twin_simulation_result")
public class PlatformTwinSimulationResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "scenario_id", nullable = false)
    @NotNull
    private Long scenarioId;

    @Column(name = "simulation_output", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String simulationOutput;

    @Column(nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal confidence;

    @Column(name = "simulated_at", nullable = false)
    @NotNull
    private LocalDateTime simulatedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getScenarioId() { return scenarioId; }
    public void setScenarioId(Long scenarioId) { this.scenarioId = scenarioId; }
    public String getSimulationOutput() { return simulationOutput; }
    public void setSimulationOutput(String simulationOutput) { this.simulationOutput = simulationOutput; }
    public BigDecimal getConfidence() { return confidence; }
    public void setConfidence(BigDecimal confidence) { this.confidence = confidence; }
    public LocalDateTime getSimulatedAt() { return simulatedAt; }
    public void setSimulatedAt(LocalDateTime simulatedAt) { this.simulatedAt = simulatedAt; }
}