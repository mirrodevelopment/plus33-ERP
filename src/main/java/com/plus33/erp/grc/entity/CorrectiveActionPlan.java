package com.plus33.erp.grc.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "grc_corrective_action_plans")
public class CorrectiveActionPlan {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "issue_id", nullable = false) private Long issueId;
    @Column(nullable = false, columnDefinition = "TEXT") private String description;
    @Column(name = "owner_id") private Long ownerId;
    @Column(name = "due_date", nullable = false) private LocalDate dueDate;
    @Column(nullable = false, length = 30) private String status = "OPEN";
    @Column(name = "closed_at") private LocalDateTime closedAt;

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Long getIssueId() { return issueId; } public void setIssueId(Long v) { this.issueId = v; }
    public String getDescription() { return description; } public void setDescription(String v) { this.description = v; }
    public Long getOwnerId() { return ownerId; } public void setOwnerId(Long v) { this.ownerId = v; }
    public LocalDate getDueDate() { return dueDate; } public void setDueDate(LocalDate v) { this.dueDate = v; }
    public String getStatus() { return status; } public void setStatus(String v) { this.status = v; }
    public LocalDateTime getClosedAt() { return closedAt; } public void setClosedAt(LocalDateTime v) { this.closedAt = v; }
}
