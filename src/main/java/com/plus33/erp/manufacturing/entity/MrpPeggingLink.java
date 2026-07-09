/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.entity
 * File              : MrpPeggingLink.java
 * Purpose           : JPA Entity representing a persistent database record in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: MrpPeggingLinkController
 * Related Service   : MrpPeggingLinkService, MrpPeggingLinkServiceImpl
 * Related Repository: MrpPeggingLinkRepository
 * Related Entity    : MrpPeggingLink
 * Related DTO       : N/A
 * Related Mapper    : MrpPeggingLinkMapper
 * Related DB Table  : mrp_pegging_links
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : MrpPeggingLinkRepository, MrpPeggingLinkMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'mrp_pegging_links'. Defines persistent domain object for Manufacturing Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.manufacturing.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code MrpPeggingLink}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'mrp_pegging_links'.</p>
 *
 * <p><b>Database Table   :</b> {@code mrp_pegging_links}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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
     * Retrieves mrp run data from the database.
     *
     * @return the MrpRun result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public MrpRun getMrpRun() { return mrpRun; }
    /**
     * Performs the setMrpRun operation in this module.
     *
     * @param mrpRun the mrpRun input value
     */
    public void setMrpRun(MrpRun mrpRun) { this.mrpRun = mrpRun; }
    /**
     * Retrieves supply type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSupplyType() { return supplyType; }
    /**
     * Performs the setSupplyType operation in this module.
     *
     * @param supplyType the supplyType input value
     */
    public void setSupplyType(String supplyType) { this.supplyType = supplyType; }
    /**
     * Retrieves supply id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getSupplyId() { return supplyId; }
    /**
     * Performs the setSupplyId operation in this module.
     *
     * @param supplyId the supplyId input value
     */
    public void setSupplyId(Long supplyId) { this.supplyId = supplyId; }
    /**
     * Retrieves demand type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDemandType() { return demandType; }
    /**
     * Performs the setDemandType operation in this module.
     *
     * @param demandType the demandType input value
     */
    public void setDemandType(String demandType) { this.demandType = demandType; }
    /**
     * Retrieves demand id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getDemandId() { return demandId; }
    /**
     * Performs the setDemandId operation in this module.
     *
     * @param demandId the demandId input value
     */
    public void setDemandId(Long demandId) { this.demandId = demandId; }
    /**
     * Retrieves pegged quantity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getPeggedQuantity() { return peggedQuantity; }
    /**
     * Performs the setPeggedQuantity operation in this module.
     *
     * @param peggedQuantity the peggedQuantity input value
     */
    public void setPeggedQuantity(BigDecimal peggedQuantity) { this.peggedQuantity = peggedQuantity; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
}