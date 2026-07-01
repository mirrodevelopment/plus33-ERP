package com.plus33.erp.grc.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "grc_control_test_results")
public class ControlTestResult {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "test_plan_id", nullable = false) private Long testPlanId;
    @Column(nullable = false, length = 20) private String result = "PENDING";
    @Column(name = "tested_by_id") private Long testedById;
    @Column(name = "tested_at", nullable = false) private LocalDateTime testedAt = LocalDateTime.now();
    @Column(columnDefinition = "TEXT") private String notes;

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Long getTestPlanId() { return testPlanId; } public void setTestPlanId(Long v) { this.testPlanId = v; }
    public String getResult() { return result; } public void setResult(String v) { this.result = v; }
    public Long getTestedById() { return testedById; } public void setTestedById(Long v) { this.testedById = v; }
    public LocalDateTime getTestedAt() { return testedAt; } public void setTestedAt(LocalDateTime v) { this.testedAt = v; }
    public String getNotes() { return notes; } public void setNotes(String v) { this.notes = v; }
}
