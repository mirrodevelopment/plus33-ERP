package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "payroll_item_breakdowns")
public class PayrollItemBreakdown {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payroll_run_item_id", nullable = false)
    private Long payrollRunItemId;

    @Column(name = "component_id", nullable = false)
    private Long componentId;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal amount = BigDecimal.ZERO;

    private String description;

    public PayrollItemBreakdown() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPayrollRunItemId() { return payrollRunItemId; }
    public void setPayrollRunItemId(Long payrollRunItemId) { this.payrollRunItemId = payrollRunItemId; }
    public Long getComponentId() { return componentId; }
    public void setComponentId(Long componentId) { this.componentId = componentId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
