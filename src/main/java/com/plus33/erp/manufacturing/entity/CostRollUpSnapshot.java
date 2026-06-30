package com.plus33.erp.manufacturing.entity;

import com.plus33.erp.inventory.entity.Product;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "cost_roll_up_snapshots")
public class CostRollUpSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bom_header_id", nullable = false)
    private BomHeader bomHeader;

    @Column(name = "snapshot_date", nullable = false)
    private LocalDate snapshotDate;

    @Column(name = "rolled_material_cost", nullable = false, precision = 22, scale = 6)
    private BigDecimal rolledMaterialCost = BigDecimal.ZERO;

    @Column(name = "rolled_labor_cost", nullable = false, precision = 22, scale = 6)
    private BigDecimal rolledLaborCost = BigDecimal.ZERO;

    @Column(name = "rolled_machine_cost", nullable = false, precision = 22, scale = 6)
    private BigDecimal rolledMachineCost = BigDecimal.ZERO;

    @Column(name = "rolled_overhead_cost", nullable = false, precision = 22, scale = 6)
    private BigDecimal rolledOverheadCost = BigDecimal.ZERO;

    @Column(name = "rolled_subcontract_cost", nullable = false, precision = 22, scale = 6)
    private BigDecimal rolledSubcontractCost = BigDecimal.ZERO;

    @Column(name = "rolled_total_cost", nullable = false, precision = 22, scale = 6)
    private BigDecimal rolledTotalCost = BigDecimal.ZERO;

    @Column(name = "run_by")
    private Long runBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public CostRollUpSnapshot() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public BomHeader getBomHeader() { return bomHeader; }
    public void setBomHeader(BomHeader bomHeader) { this.bomHeader = bomHeader; }
    public LocalDate getSnapshotDate() { return snapshotDate; }
    public void setSnapshotDate(LocalDate snapshotDate) { this.snapshotDate = snapshotDate; }
    public BigDecimal getRolledMaterialCost() { return rolledMaterialCost; }
    public void setRolledMaterialCost(BigDecimal rolledMaterialCost) { this.rolledMaterialCost = rolledMaterialCost; }
    public BigDecimal getRolledLaborCost() { return rolledLaborCost; }
    public void setRolledLaborCost(BigDecimal rolledLaborCost) { this.rolledLaborCost = rolledLaborCost; }
    public BigDecimal getRolledMachineCost() { return rolledMachineCost; }
    public void setRolledMachineCost(BigDecimal rolledMachineCost) { this.rolledMachineCost = rolledMachineCost; }
    public BigDecimal getRolledOverheadCost() { return rolledOverheadCost; }
    public void setRolledOverheadCost(BigDecimal rolledOverheadCost) { this.rolledOverheadCost = rolledOverheadCost; }
    public BigDecimal getRolledSubcontractCost() { return rolledSubcontractCost; }
    public void setRolledSubcontractCost(BigDecimal rolledSubcontractCost) { this.rolledSubcontractCost = rolledSubcontractCost; }
    public BigDecimal getRolledTotalCost() { return rolledTotalCost; }
    public void setRolledTotalCost(BigDecimal rolledTotalCost) { this.rolledTotalCost = rolledTotalCost; }
    public Long getRunBy() { return runBy; }
    public void setRunBy(Long runBy) { this.runBy = runBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
