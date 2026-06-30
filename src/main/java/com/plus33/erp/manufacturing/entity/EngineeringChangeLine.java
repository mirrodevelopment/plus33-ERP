package com.plus33.erp.manufacturing.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "engineering_change_lines")
public class EngineeringChangeLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eco_id", nullable = false)
    private EngineeringChangeOrder engineeringChangeOrder;

    @Column(name = "change_type", nullable = false, length = 30)
    private String changeType; // BOM_ADD, BOM_REMOVE, BOM_MODIFY, ROUTING_ADD, ROUTING_REMOVE, ROUTING_MODIFY

    @Column(name = "reference_type", nullable = false, length = 30)
    private String referenceType; // BOM_HEADER, BOM_LINE, ROUTING_HEADER, ROUTING_OPERATION

    @Column(name = "reference_id", nullable = false)
    private Long referenceId;

    @Column(name = "before_snapshot", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String beforeSnapshot;

    @Column(name = "after_snapshot", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String afterSnapshot;

    @Column(name = "effective_from")
    private LocalDate effectiveFrom;

    @Column(name = "sort_sequence", nullable = false)
    private Integer sortSequence = 10;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public EngineeringChangeLine() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public EngineeringChangeOrder getEngineeringChangeOrder() { return engineeringChangeOrder; }
    public void setEngineeringChangeOrder(EngineeringChangeOrder eco) { this.engineeringChangeOrder = eco; }
    public String getChangeType() { return changeType; }
    public void setChangeType(String changeType) { this.changeType = changeType; }
    public String getReferenceType() { return referenceType; }
    public void setReferenceType(String referenceType) { this.referenceType = referenceType; }
    public Long getReferenceId() { return referenceId; }
    public void setReferenceId(Long referenceId) { this.referenceId = referenceId; }
    public String getBeforeSnapshot() { return beforeSnapshot; }
    public void setBeforeSnapshot(String beforeSnapshot) { this.beforeSnapshot = beforeSnapshot; }
    public String getAfterSnapshot() { return afterSnapshot; }
    public void setAfterSnapshot(String afterSnapshot) { this.afterSnapshot = afterSnapshot; }
    public LocalDate getEffectiveFrom() { return effectiveFrom; }
    public void setEffectiveFrom(LocalDate effectiveFrom) { this.effectiveFrom = effectiveFrom; }
    public Integer getSortSequence() { return sortSequence; }
    public void setSortSequence(Integer sortSequence) { this.sortSequence = sortSequence; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
