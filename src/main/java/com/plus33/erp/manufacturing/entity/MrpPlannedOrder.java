/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.entity
 * File              : MrpPlannedOrder.java
 * Purpose           : JPA Entity representing a persistent database record in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: MrpPlannedOrderController
 * Related Service   : MrpPlannedOrderService, MrpPlannedOrderServiceImpl
 * Related Repository: MrpPlannedOrderRepository
 * Related Entity    : MrpPlannedOrder
 * Related DTO       : N/A
 * Related Mapper    : MrpPlannedOrderMapper
 * Related DB Table  : planned_orders
 * Related REST APIs : N/A
 * Depends On        : Inventory Module
 * Used By           : MrpPlannedOrderRepository, MrpPlannedOrderMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'planned_orders'. Defines persistent domain object for Manufacturing Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.manufacturing.entity;

import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.inventory.entity.UnitOfMeasure;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code MrpPlannedOrder}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'planned_orders'.</p>
 *
 * <p><b>Database Table   :</b> {@code planned_orders}</p>
 * <p><b>Module Deps      :</b> Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "planned_orders")
public class MrpPlannedOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mrp_run_id", nullable = false)
    private MrpRun mrpRun;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "order_type", nullable = false, length = 30)
    private String orderType; // PRODUCTION, PURCHASE, TRANSFER

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PlannedOrderStatus status = PlannedOrderStatus.OPEN;

    @Column(name = "required_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal requiredQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false)
    private UnitOfMeasure unit;

    @Column(name = "required_date", nullable = false)
    private LocalDate requiredDate;

    @Column(name = "planned_start_date", nullable = false)
    private LocalDate plannedStartDate;

    @Column(name = "planned_end_date", nullable = false)
    private LocalDate plannedEndDate;

    @Column(name = "demand_source", length = 50)
    private String demandSource; // SALES_ORDER, FORECAST, SAFETY_STOCK, DEPENDENT

    @Column(name = "demand_source_id")
    private Long demandSourceId;

    @Column(name = "bom_header_id")
    private Long bomHeaderId;

    @Column(name = "routing_header_id")
    private Long routingHeaderId;

    @Column(nullable = false)
    private Boolean firmed = false;

    @Column(name = "released_production_order_id")
    private Long releasedProductionOrderId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public MrpPlannedOrder() {}

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
     * Retrieves order type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getOrderType() { return orderType; }
    /**
     * Performs the setOrderType operation in this module.
     *
     * @param orderType the orderType input value
     */
    public void setOrderType(String orderType) { this.orderType = orderType; }
    /**
     * Retrieves status data from the database.
     *
     * @return the PlannedOrderStatus result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public PlannedOrderStatus getStatus() { return status; }
    /**
     * Performs the setStatus operation in this module.
     *
     * @param status status filter for narrowing query results
     */
    public void setStatus(PlannedOrderStatus status) { this.status = status; }
    /**
     * Retrieves required quantity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getRequiredQuantity() { return requiredQuantity; }
    /**
     * Performs the setRequiredQuantity operation in this module.
     *
     * @param requiredQuantity the requiredQuantity input value
     */
    public void setRequiredQuantity(BigDecimal requiredQuantity) { this.requiredQuantity = requiredQuantity; }
    /**
     * Retrieves unit data from the database.
     *
     * @return the UnitOfMeasure result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public UnitOfMeasure getUnit() { return unit; }
    /**
     * Performs the setUnit operation in this module.
     *
     * @param unit the unit input value
     */
    public void setUnit(UnitOfMeasure unit) { this.unit = unit; }
    /**
     * Retrieves required date data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getRequiredDate() { return requiredDate; }
    /**
     * Performs the setRequiredDate operation in this module.
     *
     * @param requiredDate the requiredDate input value
     */
    public void setRequiredDate(LocalDate requiredDate) { this.requiredDate = requiredDate; }
    /**
     * Retrieves planned start date data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getPlannedStartDate() { return plannedStartDate; }
    /**
     * Performs the setPlannedStartDate operation in this module.
     *
     * @param plannedStartDate the plannedStartDate input value
     */
    public void setPlannedStartDate(LocalDate plannedStartDate) { this.plannedStartDate = plannedStartDate; }
    /**
     * Retrieves planned end date data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getPlannedEndDate() { return plannedEndDate; }
    /**
     * Performs the setPlannedEndDate operation in this module.
     *
     * @param plannedEndDate the plannedEndDate input value
     */
    public void setPlannedEndDate(LocalDate plannedEndDate) { this.plannedEndDate = plannedEndDate; }
    /**
     * Retrieves demand source data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDemandSource() { return demandSource; }
    /**
     * Performs the setDemandSource operation in this module.
     *
     * @param demandSource the demandSource input value
     */
    public void setDemandSource(String demandSource) { this.demandSource = demandSource; }
    /**
     * Retrieves demand source id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getDemandSourceId() { return demandSourceId; }
    /**
     * Performs the setDemandSourceId operation in this module.
     *
     * @param demandSourceId the demandSourceId input value
     */
    public void setDemandSourceId(Long demandSourceId) { this.demandSourceId = demandSourceId; }
    /**
     * Retrieves bom header id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getBomHeaderId() { return bomHeaderId; }
    /**
     * Performs the setBomHeaderId operation in this module.
     *
     * @param bomHeaderId the bomHeaderId input value
     */
    public void setBomHeaderId(Long bomHeaderId) { this.bomHeaderId = bomHeaderId; }
    /**
     * Retrieves routing header id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getRoutingHeaderId() { return routingHeaderId; }
    /**
     * Performs the setRoutingHeaderId operation in this module.
     *
     * @param routingHeaderId the routingHeaderId input value
     */
    public void setRoutingHeaderId(Long routingHeaderId) { this.routingHeaderId = routingHeaderId; }
    /**
     * Retrieves firmed data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getFirmed() { return firmed; }
    /**
     * Performs the setFirmed operation in this module.
     *
     * @param firmed the firmed input value
     */
    public void setFirmed(Boolean firmed) { this.firmed = firmed; }
    /**
     * Retrieves released production order id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getReleasedProductionOrderId() { return releasedProductionOrderId; }
    /**
     * Performs the setReleasedProductionOrderId operation in this module.
     *
     * @param releasedProductionOrderId the releasedProductionOrderId input value
     */
    public void setReleasedProductionOrderId(Long releasedProductionOrderId) { this.releasedProductionOrderId = releasedProductionOrderId; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Getter/setter aliases to prevent compile breaks
    /**
     * Retrieves quantity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getQuantity() { return requiredQuantity; }
    /**
     * Performs the setQuantity operation in this module.
     *
     * @param quantity the quantity input value
     */
    public void setQuantity(BigDecimal quantity) { this.requiredQuantity = quantity; }
    /**
     * Retrieves suggested start date data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getSuggestedStartDate() { return plannedStartDate; }
    /**
     * Performs the setSuggestedStartDate operation in this module.
     *
     * @param suggestedStartDate the suggestedStartDate input value
     */
    public void setSuggestedStartDate(LocalDate suggestedStartDate) { this.plannedStartDate = suggestedStartDate; }
    /**
     * Retrieves suggested due date data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getSuggestedDueDate() { return plannedEndDate; }
    /**
     * Performs the setSuggestedDueDate operation in this module.
     *
     * @param suggestedDueDate the suggestedDueDate input value
     */
    public void setSuggestedDueDate(LocalDate suggestedDueDate) { this.plannedEndDate = suggestedDueDate; }
    /**
     * Retrieves source type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSourceType() { return demandSource; }
    /**
     * Performs the setSourceType operation in this module.
     *
     * @param sourceType the sourceType input value
     */
    public void setSourceType(String sourceType) { this.demandSource = sourceType; }
    /**
     * Retrieves source reference id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getSourceReferenceId() { return demandSourceId; }
    /**
     * Performs the setSourceReferenceId operation in this module.
     *
     * @param sourceReferenceId the sourceReferenceId input value
     */
    public void setSourceReferenceId(Long sourceReferenceId) { this.demandSourceId = sourceReferenceId; }
}