package com.plus33.erp.manufacturing.dto;

import java.time.LocalDate;

public class EngineeringChangeLineDto {
    private Long id;
    private Long ecoId;
    private String changeType; // BOM_ADD, BOM_REMOVE, BOM_MODIFY, ROUTING_ADD, ROUTING_REMOVE, ROUTING_MODIFY
    private String referenceType; // BOM_HEADER, BOM_LINE, ROUTING_HEADER, ROUTING_OPERATION
    private Long referenceId;
    private String beforeSnapshot;
    private String afterSnapshot;
    private LocalDate effectiveFrom;
    private Integer sortSequence;
    private String notes;

    public EngineeringChangeLineDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getEcoId() { return ecoId; }
    public void setEcoId(Long ecoId) { this.ecoId = ecoId; }
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
}
