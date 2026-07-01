package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_k8s_pod_status")
public class PlatformK8sPodStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "pod_name", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String podName;

    @Column(nullable = false)
    @NotNull
    @Size(max = 100)
    private String namespace;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "CREATED";

    @Column(name = "node_ip")
    @Size(max = 100)
    private String nodeIp;

    @Column(nullable = false)
    @NotNull
    private Integer restarts = 0;

    @Column(name = "updated_at", nullable = false)
    @NotNull
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getPodName() { return podName; }
    public void setPodName(String podName) { this.podName = podName; }
    public String getNamespace() { return namespace; }
    public void setNamespace(String namespace) { this.namespace = namespace; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getNodeIp() { return nodeIp; }
    public void setNodeIp(String nodeIp) { this.nodeIp = nodeIp; }
    public Integer getRestarts() { return restarts; }
    public void setRestarts(Integer restarts) { this.restarts = restarts; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}