package com.plus33.erp.integration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "integration_gateway_usage_log")
public class IntegrationGatewayUsageLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "api_key", nullable = false)
    @NotNull
    @Size(max = 100)
    private String apiKey;

    @Column(name = "request_path", nullable = false)
    @NotNull
    @Size(max = 250)
    private String requestPath;

    @Column(name = "http_method", nullable = false)
    @NotNull
    @Size(max = 10)
    private String httpMethod;

    @Column(name = "status_code", nullable = false)
    @NotNull
    private Integer statusCode;

    @Column(name = "response_time_ms", nullable = false)
    @NotNull
    private Long responseTimeMs;

    @Column(name = "called_at", nullable = false, updatable = false)
    @NotNull
    private LocalDateTime calledAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }
    public String getRequestPath() { return requestPath; }
    public void setRequestPath(String requestPath) { this.requestPath = requestPath; }
    public String getHttpMethod() { return httpMethod; }
    public void setHttpMethod(String httpMethod) { this.httpMethod = httpMethod; }
    public Integer getStatusCode() { return statusCode; }
    public void setStatusCode(Integer statusCode) { this.statusCode = statusCode; }
    public Long getResponseTimeMs() { return responseTimeMs; }
    public void setResponseTimeMs(Long responseTimeMs) { this.responseTimeMs = responseTimeMs; }
    public LocalDateTime getCalledAt() { return calledAt; }
    public void setCalledAt(LocalDateTime calledAt) { this.calledAt = calledAt; }
}