package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "platform_cost_allocation")
public class PlatformCostAllocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "cost_center_id", nullable = false)
    @NotNull
    private Long costCenterId;

    @Column(name = "resource_id", nullable = false)
    @NotNull
    @Size(max = 250)
    private String resourceId;

    @Column(name = "allocation_ratio", nullable = false, precision = 5, scale = 2)
    @NotNull
    private java.math.BigDecimal allocationRatio;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public Long getCostCenterId() { return costCenterId; }
    public void setCostCenterId(Long costCenterId) { this.costCenterId = costCenterId; }
    public String getResourceId() { return resourceId; }
    public void setResourceId(String resourceId) { this.resourceId = resourceId; }
    public java.math.BigDecimal getAllocationRatio() { return allocationRatio; }
    public void setAllocationRatio(java.math.BigDecimal allocationRatio) { this.allocationRatio = allocationRatio; }
}