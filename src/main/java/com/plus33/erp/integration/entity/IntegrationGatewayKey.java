package com.plus33.erp.integration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "integration_gateway_key")
public class IntegrationGatewayKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "api_key", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String apiKey;

    @Column(name = "partner_code", nullable = false)
    @NotNull
    @Size(max = 100)
    private String partnerCode;

    @Column(name = "rate_limit_per_min", nullable = false)
    @NotNull
    private Integer rateLimitPerMin = 60;

    @Column(name = "quota_per_day", nullable = false)
    @NotNull
    private Integer quotaPerDay = 10000;

    @Column(name = "allowed_routes", nullable = false)
    @NotNull
    @Size(max = 500)
    private String allowedRoutes = "*";

    @Column(nullable = false)
    @NotNull
    private Boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }
    public String getPartnerCode() { return partnerCode; }
    public void setPartnerCode(String partnerCode) { this.partnerCode = partnerCode; }
    public Integer getRateLimitPerMin() { return rateLimitPerMin; }
    public void setRateLimitPerMin(Integer rateLimitPerMin) { this.rateLimitPerMin = rateLimitPerMin; }
    public Integer getQuotaPerDay() { return quotaPerDay; }
    public void setQuotaPerDay(Integer quotaPerDay) { this.quotaPerDay = quotaPerDay; }
    public String getAllowedRoutes() { return allowedRoutes; }
    public void setAllowedRoutes(String allowedRoutes) { this.allowedRoutes = allowedRoutes; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}