package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bi_mdm_merge_request")
public class MdmMergeRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "request_code", nullable = false, unique = true)
    private String requestCode;
    @Column(name = "record_type", nullable = false)
    private String recordType;
    @Column(name = "source_system_a", nullable = false)
    private String sourceSystemA;
    @Column(name = "source_dim_id_a", nullable = false)
    private Long sourceDimIdA;
    @Column(name = "source_system_b", nullable = false)
    private String sourceSystemB;
    @Column(name = "source_dim_id_b", nullable = false)
    private Long sourceDimIdB;
    @Column(nullable = false)
    private String status = "REQUESTED";
    @Column(name = "requested_by", nullable = false)
    private String requestedBy;
    private String comments;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRequestCode() { return requestCode; }
    public void setRequestCode(String requestCode) { this.requestCode = requestCode; }
    public String getRecordType() { return recordType; }
    public void setRecordType(String recordType) { this.recordType = recordType; }
    public String getSourceSystemA() { return sourceSystemA; }
    public void setSourceSystemA(String sourceSystemA) { this.sourceSystemA = sourceSystemA; }
    public Long getSourceDimIdA() { return sourceDimIdA; }
    public void setSourceDimIdA(Long sourceDimIdA) { this.sourceDimIdA = sourceDimIdA; }
    public String getSourceSystemB() { return sourceSystemB; }
    public void setSourceSystemB(String sourceSystemB) { this.sourceSystemB = sourceSystemB; }
    public Long getSourceDimIdB() { return sourceDimIdB; }
    public void setSourceDimIdB(Long sourceDimIdB) { this.sourceDimIdB = sourceDimIdB; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRequestedBy() { return requestedBy; }
    public void setRequestedBy(String requestedBy) { this.requestedBy = requestedBy; }
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}