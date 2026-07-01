package com.plus33.erp.grc.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "grc_policy_acknowledgements",
    uniqueConstraints = @UniqueConstraint(columnNames = {"policy_version_id", "employee_id"}))
public class PolicyAcknowledgement {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "policy_version_id", nullable = false) private Long policyVersionId;
    @Column(name = "employee_id", nullable = false) private Long employeeId;
    @Column(name = "acknowledged_at", nullable = false) private LocalDateTime acknowledgedAt = LocalDateTime.now();

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Long getPolicyVersionId() { return policyVersionId; } public void setPolicyVersionId(Long v) { this.policyVersionId = v; }
    public Long getEmployeeId() { return employeeId; } public void setEmployeeId(Long v) { this.employeeId = v; }
    public LocalDateTime getAcknowledgedAt() { return acknowledgedAt; } public void setAcknowledgedAt(LocalDateTime v) { this.acknowledgedAt = v; }
}
