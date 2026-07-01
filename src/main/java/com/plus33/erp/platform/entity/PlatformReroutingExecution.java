package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_rerouting_execution")
public class PlatformReroutingExecution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rerouting_id", nullable = false)
    @NotNull
    private Long reroutingId;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "EXECUTED";

    @Column(name = "executed_at", nullable = false)
    @NotNull
    private LocalDateTime executedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getReroutingId() { return reroutingId; }
    public void setReroutingId(Long reroutingId) { this.reroutingId = reroutingId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getExecutedAt() { return executedAt; }
    public void setExecutedAt(LocalDateTime executedAt) { this.executedAt = executedAt; }
}