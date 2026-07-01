package com.plus33.erp.hcm.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "hcm_successors")
public class Successor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "talent_pool_id", nullable = false)
    private Long talentPoolId;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "readiness_score", nullable = false)
    private BigDecimal readinessScore = BigDecimal.ZERO;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getTalentPoolId() { return talentPoolId; }
    public void setTalentPoolId(Long talentPoolId) { this.talentPoolId = talentPoolId; }
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
    public BigDecimal getReadinessScore() { return readinessScore; }
    public void setReadinessScore(BigDecimal readinessScore) { this.readinessScore = readinessScore; }
}
