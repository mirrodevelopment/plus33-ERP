package com.plus33.erp.integration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "bi_broker_registry")
public class BiBrokerRegistry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "broker_name", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String brokerName;

    @Column(name = "broker_type", nullable = false)
    @NotNull
    @Size(max = 50)
    private String brokerType;

    @Column(name = "connection_url", nullable = false)
    @NotNull
    @Size(max = 250)
    private String connectionUrl;

    @Column(nullable = false)
    @NotNull
    private Boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    @NotNull
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBrokerName() { return brokerName; }
    public void setBrokerName(String brokerName) { this.brokerName = brokerName; }
    public String getBrokerType() { return brokerType; }
    public void setBrokerType(String brokerType) { this.brokerType = brokerType; }
    public String getConnectionUrl() { return connectionUrl; }
    public void setConnectionUrl(String connectionUrl) { this.connectionUrl = connectionUrl; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}