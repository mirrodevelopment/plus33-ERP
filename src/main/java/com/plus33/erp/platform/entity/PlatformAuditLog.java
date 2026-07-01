package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_audit_log")
public class PlatformAuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "action_name", nullable = false)
    @NotNull
    @Size(max = 150)
    private String actionName;

    @Column(name = "user_identity", nullable = false)
    @NotNull
    @Size(max = 100)
    private String userIdentity;

    @Column(name = "trace_context")
    @Size(max = 250)
    private String traceContext;

    @Column(name = "payload_diff", columnDefinition = "TEXT")
    private String payloadDiff;

    @Column(name = "created_at", nullable = false, updatable = false)
    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getActionName() { return actionName; }
    public void setActionName(String actionName) { this.actionName = actionName; }
    public String getUserIdentity() { return userIdentity; }
    public void setUserIdentity(String userIdentity) { this.userIdentity = userIdentity; }
    public String getTraceContext() { return traceContext; }
    public void setTraceContext(String traceContext) { this.traceContext = traceContext; }
    public String getPayloadDiff() { return payloadDiff; }
    public void setPayloadDiff(String payloadDiff) { this.payloadDiff = payloadDiff; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}