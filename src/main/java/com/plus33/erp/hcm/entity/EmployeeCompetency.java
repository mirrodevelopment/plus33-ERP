package com.plus33.erp.hcm.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "hcm_employee_competencies")
public class EmployeeCompetency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "competency_id", nullable = false)
    private Long competencyId;

    @Column(nullable = false)
    private BigDecimal rating = BigDecimal.ZERO;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
    public Long getCompetencyId() { return competencyId; }
    public void setCompetencyId(Long competencyId) { this.competencyId = competencyId; }
    public BigDecimal getRating() { return rating; }
    public void setRating(BigDecimal rating) { this.rating = rating; }
}
