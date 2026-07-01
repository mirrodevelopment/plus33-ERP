package com.plus33.erp.grc.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "grc_audit_engagements")
public class AuditEngagement {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "program_id", nullable = false) private Long programId;
    @Column(name = "audit_universe_id", nullable = false) private Long auditUniverseId;
    @Column(name = "engagement_number", nullable = false, unique = true, length = 50) private String engagementNumber;
    @Column(nullable = false, length = 200) private String title;
    @Column(nullable = false, length = 30) private String status = "PLANNED";
    @Column(name = "lead_auditor_id") private Long leadAuditorId;
    @Column(name = "start_date") private LocalDate startDate;
    @Column(name = "end_date") private LocalDate endDate;

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Long getProgramId() { return programId; } public void setProgramId(Long v) { this.programId = v; }
    public Long getAuditUniverseId() { return auditUniverseId; } public void setAuditUniverseId(Long v) { this.auditUniverseId = v; }
    public String getEngagementNumber() { return engagementNumber; } public void setEngagementNumber(String v) { this.engagementNumber = v; }
    public String getTitle() { return title; } public void setTitle(String v) { this.title = v; }
    public String getStatus() { return status; } public void setStatus(String v) { this.status = v; }
    public Long getLeadAuditorId() { return leadAuditorId; } public void setLeadAuditorId(Long v) { this.leadAuditorId = v; }
    public LocalDate getStartDate() { return startDate; } public void setStartDate(LocalDate v) { this.startDate = v; }
    public LocalDate getEndDate() { return endDate; } public void setEndDate(LocalDate v) { this.endDate = v; }
}
