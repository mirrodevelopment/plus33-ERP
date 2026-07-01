package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bi_kpi_formula_version")
public class BiKpiFormulaVersion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "kpi_id", nullable = false) private Long kpiId;
    @Column(name = "version_number", nullable = false) private Integer versionNumber = 1;
    @Column(name = "formula_expression", nullable = false, columnDefinition = "TEXT") private String formulaExpression;
    @Column(name = "compiled_expression", columnDefinition = "TEXT") private String compiledExpression;
    @Column(name = "effective_from", nullable = false) private LocalDate effectiveFrom;
    @Column(name = "effective_to") private LocalDate effectiveTo;
    @Column(name = "is_current", nullable = false) private Boolean isCurrent = true;
    @Column(name = "published_by", length = 100) private String publishedBy;
    @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Long getKpiId() { return kpiId; } public void setKpiId(Long v) { this.kpiId = v; }
    public Integer getVersionNumber() { return versionNumber; } public void setVersionNumber(Integer v) { this.versionNumber = v; }
    public String getFormulaExpression() { return formulaExpression; } public void setFormulaExpression(String v) { this.formulaExpression = v; }
    public String getCompiledExpression() { return compiledExpression; } public void setCompiledExpression(String v) { this.compiledExpression = v; }
    public LocalDate getEffectiveFrom() { return effectiveFrom; } public void setEffectiveFrom(LocalDate v) { this.effectiveFrom = v; }
    public LocalDate getEffectiveTo() { return effectiveTo; } public void setEffectiveTo(LocalDate v) { this.effectiveTo = v; }
    public Boolean getIsCurrent() { return isCurrent; } public void setIsCurrent(Boolean v) { this.isCurrent = v; }
    public String getPublishedBy() { return publishedBy; } public void setPublishedBy(String v) { this.publishedBy = v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
