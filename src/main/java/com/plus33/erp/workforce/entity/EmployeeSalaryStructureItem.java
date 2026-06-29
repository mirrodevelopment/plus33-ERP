package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "employee_salary_structure_items")
public class EmployeeSalaryStructureItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "structure_id", nullable = false)
    private Long structureId;

    @Column(name = "component_id", nullable = false)
    private Long componentId;

    @Column(nullable = false, precision = 14, scale = 4)
    private BigDecimal amount = BigDecimal.ZERO;

    @Column(precision = 7, scale = 4)
    private BigDecimal percentage;

    @Column(name = "formula_expression")
    private String formulaExpression;

    @Column(nullable = false)
    private boolean active = true;

    public EmployeeSalaryStructureItem() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getStructureId() { return structureId; }
    public void setStructureId(Long structureId) { this.structureId = structureId; }
    public Long getComponentId() { return componentId; }
    public void setComponentId(Long componentId) { this.componentId = componentId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public BigDecimal getPercentage() { return percentage; }
    public void setPercentage(BigDecimal percentage) { this.percentage = percentage; }
    public String getFormulaExpression() { return formulaExpression; }
    public void setFormulaExpression(String formulaExpression) { this.formulaExpression = formulaExpression; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
