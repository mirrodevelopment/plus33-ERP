/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.entity
 * File              : CostRollUpSnapshot.java
 * Purpose           : JPA Entity representing a persistent database record in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CostRollUpSnapshotController
 * Related Service   : CostRollUpSnapshotService, CostRollUpSnapshotServiceImpl
 * Related Repository: CostRollUpSnapshotRepository
 * Related Entity    : CostRollUpSnapshot
 * Related DTO       : N/A
 * Related Mapper    : CostRollUpSnapshotMapper
 * Related DB Table  : cost_roll_up_snapshots
 * Related REST APIs : N/A
 * Depends On        : Inventory Module
 * Used By           : CostRollUpSnapshotRepository, CostRollUpSnapshotMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'cost_roll_up_snapshots'. Defines persistent domain object for Manufacturing Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.manufacturing.entity;

import com.plus33.erp.inventory.entity.Product;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code CostRollUpSnapshot}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'cost_roll_up_snapshots'.</p>
 *
 * <p><b>Database Table   :</b> {@code cost_roll_up_snapshots}</p>
 * <p><b>Module Deps      :</b> Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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

    /**
     * Retrieves id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getId() { return id; }
    /**
     * Performs the setId operation in this module.
     *
     * @param id the unique database ID of the resource
     */
    public void setId(Long id) { this.id = id; }
    /**
     * Retrieves company id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCompanyId() { return companyId; }
    /**
     * Performs the setCompanyId operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     */
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    /**
     * Retrieves product data from the database.
     *
     * @return the Product result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Product getProduct() { return product; }
    /**
     * Performs the setProduct operation in this module.
     *
     * @param product the product input value
     */
    public void setProduct(Product product) { this.product = product; }
    /**
     * Retrieves bom header data from the database.
     *
     * @return the BomHeader result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BomHeader getBomHeader() { return bomHeader; }
    /**
     * Performs the setBomHeader operation in this module.
     *
     * @param bomHeader the bomHeader input value
     */
    public void setBomHeader(BomHeader bomHeader) { this.bomHeader = bomHeader; }
    /**
     * Retrieves snapshot date data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getSnapshotDate() { return snapshotDate; }
    /**
     * Performs the setSnapshotDate operation in this module.
     *
     * @param snapshotDate the snapshotDate input value
     */
    public void setSnapshotDate(LocalDate snapshotDate) { this.snapshotDate = snapshotDate; }
    /**
     * Retrieves rolled material cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getRolledMaterialCost() { return rolledMaterialCost; }
    /**
     * Performs the setRolledMaterialCost operation in this module.
     *
     * @param rolledMaterialCost the rolledMaterialCost input value
     */
    public void setRolledMaterialCost(BigDecimal rolledMaterialCost) { this.rolledMaterialCost = rolledMaterialCost; }
    /**
     * Retrieves rolled labor cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getRolledLaborCost() { return rolledLaborCost; }
    /**
     * Performs the setRolledLaborCost operation in this module.
     *
     * @param rolledLaborCost the rolledLaborCost input value
     */
    public void setRolledLaborCost(BigDecimal rolledLaborCost) { this.rolledLaborCost = rolledLaborCost; }
    /**
     * Retrieves rolled machine cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getRolledMachineCost() { return rolledMachineCost; }
    /**
     * Performs the setRolledMachineCost operation in this module.
     *
     * @param rolledMachineCost the rolledMachineCost input value
     */
    public void setRolledMachineCost(BigDecimal rolledMachineCost) { this.rolledMachineCost = rolledMachineCost; }
    /**
     * Retrieves rolled overhead cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getRolledOverheadCost() { return rolledOverheadCost; }
    /**
     * Performs the setRolledOverheadCost operation in this module.
     *
     * @param rolledOverheadCost the rolledOverheadCost input value
     */
    public void setRolledOverheadCost(BigDecimal rolledOverheadCost) { this.rolledOverheadCost = rolledOverheadCost; }
    /**
     * Retrieves rolled subcontract cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getRolledSubcontractCost() { return rolledSubcontractCost; }
    /**
     * Performs the setRolledSubcontractCost operation in this module.
     *
     * @param rolledSubcontractCost the rolledSubcontractCost input value
     */
    public void setRolledSubcontractCost(BigDecimal rolledSubcontractCost) { this.rolledSubcontractCost = rolledSubcontractCost; }
    /**
     * Retrieves rolled total cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getRolledTotalCost() { return rolledTotalCost; }
    /**
     * Performs the setRolledTotalCost operation in this module.
     *
     * @param rolledTotalCost the rolledTotalCost input value
     */
    public void setRolledTotalCost(BigDecimal rolledTotalCost) { this.rolledTotalCost = rolledTotalCost; }
    /**
     * Retrieves run by data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getRunBy() { return runBy; }
    /**
     * Performs the setRunBy operation in this module.
     *
     * @param runBy the runBy input value
     */
    public void setRunBy(Long runBy) { this.runBy = runBy; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
}