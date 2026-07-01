package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_distributed_lock")
public class PlatformDistributedLock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lock_name", nullable = false, unique = true)
    @NotNull
    @Size(max = 150)
    private String lockName;

    @Column(name = "owner_node", nullable = false)
    @NotNull
    @Size(max = 100)
    private String ownerNode;

    @Column(name = "expires_at", nullable = false)
    @NotNull
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    @NotNull
    private LocalDateTime heartbeat = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLockName() { return lockName; }
    public void setLockName(String lockName) { this.lockName = lockName; }
    public String getOwnerNode() { return ownerNode; }
    public void setOwnerNode(String ownerNode) { this.ownerNode = ownerNode; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    public LocalDateTime getHeartbeat() { return heartbeat; }
    public void setHeartbeat(LocalDateTime heartbeat) { this.heartbeat = heartbeat; }
}