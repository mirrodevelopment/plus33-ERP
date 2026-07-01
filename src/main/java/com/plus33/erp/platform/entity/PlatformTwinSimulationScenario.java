package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "platform_twin_simulation_scenario")
public class PlatformTwinSimulationScenario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "scenario_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String scenarioCode;

    @Column(name = "scenario_name", nullable = false)
    @NotNull
    @Size(max = 150)
    private String scenarioName;

    @Column(name = "config_variables", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String configVariables;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getScenarioCode() { return scenarioCode; }
    public void setScenarioCode(String scenarioCode) { this.scenarioCode = scenarioCode; }
    public String getScenarioName() { return scenarioName; }
    public void setScenarioName(String scenarioName) { this.scenarioName = scenarioName; }
    public String getConfigVariables() { return configVariables; }
    public void setConfigVariables(String configVariables) { this.configVariables = configVariables; }
}