package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "payroll_cost_allocations")
public class PayrollCostAllocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payroll_run_item_id", nullable = false)
    private Long payrollRunItemId;

    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "cost_center_id")
    private Long costCenterId;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "allocation_percentage", nullable = false, precision = 5, scale = 2)
    private BigDecimal allocationPercentage = new BigDecimal("100.00");

    public PayrollCostAllocation() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPayrollRunItemId() { return payrollRunItemId; }
    public void setPayrollRunItemId(Long payrollRunItemId) { this.payrollRunItemId = payrollRunItemId; }
    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
    public Long getCostCenterId() { return costCenterId; }
    public void setCostCenterId(Long costCenterId) { this.costCenterId = costCenterId; }
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public BigDecimal getAllocationPercentage() { return allocationPercentage; }
    public void setAllocationPercentage(BigDecimal allocationPercentage) { this.allocationPercentage = allocationPercentage; }
}
