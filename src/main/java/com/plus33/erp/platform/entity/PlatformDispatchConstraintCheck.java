package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_dispatch_constraint_check")
public class PlatformDispatchConstraintCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dispatch_id", nullable = false)
    @NotNull
    private Long dispatchId;

    @Column(name = "constraint_type", nullable = false)
    @NotNull
    @Size(max = 100)
    private String constraintType; // Capacity, ShiftLimits, DeliveryWindows

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status; // PASSED, VIOLATED

    @Size(max = 500)
    private String reason;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String severity; // INFO, WARNING, CRITICAL

    @Column(name = "checked_at", nullable = false)
    @NotNull
    private LocalDateTime checkedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getDispatchId() { return dispatchId; }
    public void setDispatchId(Long dispatchId) { this.dispatchId = dispatchId; }
    public String getConstraintType() { return constraintType; }
    public void setConstraintType(String constraintType) { this.constraintType = constraintType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    public LocalDateTime getCheckedAt() { return checkedAt; }
    public void setCheckedAt(LocalDateTime checkedAt) { this.checkedAt = checkedAt; }
}