package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "bi_mdm_steward_decision")
public class MdmStewardDecision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "steward_assignment_id", nullable = false)
    @NotNull
    private MdmStewardAssignment stewardAssignment;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String decision;

    private String notes;

    @Column(name = "decided_at", nullable = false)
    @NotNull
    private LocalDateTime decidedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public MdmStewardAssignment getStewardAssignment() { return stewardAssignment; }
    public void setStewardAssignment(MdmStewardAssignment stewardAssignment) { this.stewardAssignment = stewardAssignment; }
    public String getDecision() { return decision; }
    public void setDecision(String decision) { this.decision = decision; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDateTime getDecidedAt() { return decidedAt; }
    public void setDecidedAt(LocalDateTime decidedAt) { this.decidedAt = decidedAt; }
}