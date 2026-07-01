package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bi_mdm_steward_assignment")
public class MdmStewardAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "merge_request_id", nullable = false)
    private Long mergeRequestId;
    @Column(name = "steward_user", nullable = false)
    private String stewardUser;
    @Column(name = "assigned_at", nullable = false)
    private LocalDateTime assignedAt = LocalDateTime.now();
    @Column(name = "due_at")
    private LocalDateTime dueAt;
    @Column(nullable = false)
    private String status = "ASSIGNED";

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getMergeRequestId() { return mergeRequestId; }
    public void setMergeRequestId(Long mergeRequestId) { this.mergeRequestId = mergeRequestId; }
    public String getStewardUser() { return stewardUser; }
    public void setStewardUser(String stewardUser) { this.stewardUser = stewardUser; }
    public LocalDateTime getAssignedAt() { return assignedAt; }
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }
    public LocalDateTime getDueAt() { return dueAt; }
    public void setDueAt(LocalDateTime dueAt) { this.dueAt = dueAt; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}