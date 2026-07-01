package com.plus33.erp.project.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "project_resources")
public class ProjectResource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(unique = true, length = 100)
    private String email;

    @Column(name = "capacity_hours_per_week", nullable = false)
    private BigDecimal capacityHoursPerWeek = new BigDecimal("40.00");

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public BigDecimal getCapacityHoursPerWeek() { return capacityHoursPerWeek; }
    public void setCapacityHoursPerWeek(BigDecimal capacityHoursPerWeek) { this.capacityHoursPerWeek = capacityHoursPerWeek; }
}
