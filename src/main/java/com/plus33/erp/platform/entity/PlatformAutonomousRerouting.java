package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@Table(name = "platform_autonomous_rerouting")
public class PlatformAutonomousRerouting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transit_route_id", nullable = false)
    @NotNull
    private Long transitRouteId;

    @Column(name = "policy_id", nullable = false)
    @NotNull
    private Long policyId;

    @Column(nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal confidence;

    @Column(name = "suggested_route_json", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String suggestedRouteJson;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "PENDING";

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getTransitRouteId() { return transitRouteId; }
    public void setTransitRouteId(Long transitRouteId) { this.transitRouteId = transitRouteId; }
    public Long getPolicyId() { return policyId; }
    public void setPolicyId(Long policyId) { this.policyId = policyId; }
    public BigDecimal getConfidence() { return confidence; }
    public void setConfidence(BigDecimal confidence) { this.confidence = confidence; }
    public String getSuggestedRouteJson() { return suggestedRouteJson; }
    public void setSuggestedRouteJson(String suggestedRouteJson) { this.suggestedRouteJson = suggestedRouteJson; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}