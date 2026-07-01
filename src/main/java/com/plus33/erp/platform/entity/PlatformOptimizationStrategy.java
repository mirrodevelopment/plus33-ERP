package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "platform_optimization_strategy")
public class PlatformOptimizationStrategy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "strategy_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String strategyCode;

    @Column(name = "strategy_name", nullable = false)
    @NotNull
    @Size(max = 150)
    private String strategyName;

    @Column(name = "parameters_json", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String parametersJson;

    @Column(nullable = false)
    @NotNull
    private Boolean active = true;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getStrategyCode() { return strategyCode; }
    public void setStrategyCode(String strategyCode) { this.strategyCode = strategyCode; }
    public String getStrategyName() { return strategyName; }
    public void setStrategyName(String strategyName) { this.strategyName = strategyName; }
    public String getParametersJson() { return parametersJson; }
    public void setParametersJson(String parametersJson) { this.parametersJson = parametersJson; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}