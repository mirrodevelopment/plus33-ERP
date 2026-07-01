package com.plus33.erp.integration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "integration_saga")
public class IntegrationSaga {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "saga_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String sagaCode;

    @Column(name = "saga_type", nullable = false)
    @NotNull
    @Size(max = 100)
    private String sagaType;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "CREATED";

    @Column(name = "payload_json", columnDefinition = "TEXT")
    private String payloadJson;

    @Column(name = "created_at", nullable = false, updatable = false)
    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    @NotNull
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getSagaCode() { return sagaCode; }
    public void setSagaCode(String sagaCode) { this.sagaCode = sagaCode; }
    public String getSagaType() { return sagaType; }
    public void setSagaType(String sagaType) { this.sagaType = sagaType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPayloadJson() { return payloadJson; }
    public void setPayloadJson(String payloadJson) { this.payloadJson = payloadJson; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}