package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "bi_mdm_steward_assignment")
public class MdmStewardAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merge_request_id", nullable = false)
    @NotNull
    private MdmMergeRequest mergeRequest;

    @Column(name = "steward_user", nullable = false)
    @NotNull
    @Size(max = 100)
    private String stewardUser;

    @Column(name = "assigned_at", nullable = false)
    @NotNull
    private LocalDateTime assignedAt = LocalDateTime.now();

    @Column(name = "due_at")
    private LocalDateTime dueAt;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "ASSIGNED";

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public MdmMergeRequest getMergeRequest() { return mergeRequest; }
    public void setMergeRequest(MdmMergeRequest mergeRequest) { this.mergeRequest = mergeRequest; }
    public String getStewardUser() { return stewardUser; }
    public void setStewardUser(String stewardUser) { this.stewardUser = stewardUser; }
    public LocalDateTime getAssignedAt() { return assignedAt; }
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }
    public LocalDateTime getDueAt() { return dueAt; }
    public void setDueAt(LocalDateTime dueAt) { this.dueAt = dueAt; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}