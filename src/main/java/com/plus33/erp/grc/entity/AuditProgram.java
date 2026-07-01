package com.plus33.erp.grc.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "grc_audit_programs")
public class AuditProgram {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "company_id", nullable = false) private Long companyId;
    @Column(name = "program_name", nullable = false, length = 150) private String programName;
    @Column(name = "fiscal_year", nullable = false) private Integer fiscalYear;
    @Column(nullable = false, length = 30) private String status = "PLANNED";
    @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; } public void setCompanyId(Long v) { this.companyId = v; }
    public String getProgramName() { return programName; } public void setProgramName(String v) { this.programName = v; }
    public Integer getFiscalYear() { return fiscalYear; } public void setFiscalYear(Integer v) { this.fiscalYear = v; }
    public String getStatus() { return status; } public void setStatus(String v) { this.status = v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
