package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_deployment_group")
public class PlatformDeploymentGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "group_name", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String groupName;

    @Column(name = "active_router", nullable = false)
    @NotNull
    private Boolean activeRouter = false;

    @Column(name = "canary_weight", nullable = false)
    @NotNull
    private Integer canaryWeight = 0;

    @Column(name = "target_version", nullable = false)
    @NotNull
    @Size(max = 50)
    private String targetVersion;

    @Column(name = "updated_at", nullable = false)
    @NotNull
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }
    public Boolean getActiveRouter() { return activeRouter; }
    public void setActiveRouter(Boolean activeRouter) { this.activeRouter = activeRouter; }
    public Integer getCanaryWeight() { return canaryWeight; }
    public void setCanaryWeight(Integer canaryWeight) { this.canaryWeight = canaryWeight; }
    public String getTargetVersion() { return targetVersion; }
    public void setTargetVersion(String targetVersion) { this.targetVersion = targetVersion; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}