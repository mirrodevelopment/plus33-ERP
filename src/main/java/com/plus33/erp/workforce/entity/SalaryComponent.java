package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "salary_components")
public class SalaryComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(nullable = false, length = 50)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "component_type", nullable = false)
    private SalaryComponentType componentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "calculation_method", nullable = false)
    private CalculationMethod calculationMethod;

    @Column(name = "gl_account_id")
    private Long glAccountId;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    public SalaryComponent() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public SalaryComponentType getComponentType() { return componentType; }
    public void setComponentType(SalaryComponentType componentType) { this.componentType = componentType; }
    public CalculationMethod getCalculationMethod() { return calculationMethod; }
    public void setCalculationMethod(CalculationMethod calculationMethod) { this.calculationMethod = calculationMethod; }
    public Long getGlAccountId() { return glAccountId; }
    public void setGlAccountId(Long glAccountId) { this.glAccountId = glAccountId; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
