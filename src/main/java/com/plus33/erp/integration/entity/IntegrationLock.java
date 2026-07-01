package com.plus33.erp.integration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "integration_lock")
public class IntegrationLock {
    @Id
    @Column(name = "lock_key")
    @NotNull
    @Size(max = 250)
    private String lockKey;

    @Column(name = "owner_id", nullable = false)
    @NotNull
    @Size(max = 250)
    private String ownerId;

    @Column(name = "expires_at", nullable = false)
    @NotNull
    private LocalDateTime expiresAt;

    public String getLockKey() { return lockKey; }
    public void setLockKey(String lockKey) { this.lockKey = lockKey; }
    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
}