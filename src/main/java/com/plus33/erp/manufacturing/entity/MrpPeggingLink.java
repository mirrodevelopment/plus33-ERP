package com.plus33.erp.manufacturing.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "mrp_pegging_links")
public class MrpPeggingLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mrp_run_id", nullable = false)
    private MrpRun mrpRun;

    @Column(name = "supply_type", nullable = false, length = 30)
    private String supplyType; // PLANNED_PRODUCTION, PLANNED_PURCHASE, PRODUCTION_ORDER, PURCHASE_ORDER, STOCK

    @Column(name = "supply_id", nullable = false)
    private Long supplyId;

    @Column(name = "demand_type", nullable = false, length = 30)
    private String demandType; // SALES_ORDER, FORECAST, PRODUCTION_ORDER, SAFETY_STOCK

    @Column(name = "demand_id", nullable = false)
    private Long demandId;

    @Column(name = "pegged_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal peggedQuantity;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public MrpPeggingLink() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public MrpRun getMrpRun() { return mrpRun; }
    public void setMrpRun(MrpRun mrpRun) { this.mrpRun = mrpRun; }
    public String getSupplyType() { return supplyType; }
    public void setSupplyType(String supplyType) { this.supplyType = supplyType; }
    public Long getSupplyId() { return supplyId; }
    public void setSupplyId(Long supplyId) { this.supplyId = supplyId; }
    public String getDemandType() { return demandType; }
    public void setDemandType(String demandType) { this.demandType = demandType; }
    public Long getDemandId() { return demandId; }
    public void setDemandId(Long demandId) { this.demandId = demandId; }
    public BigDecimal getPeggedQuantity() { return peggedQuantity; }
    public void setPeggedQuantity(BigDecimal peggedQuantity) { this.peggedQuantity = peggedQuantity; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
