/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.entity
 * File              : RoutingHeader.java
 * Purpose           : JPA Entity representing a persistent database record in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: RoutingHeaderController
 * Related Service   : RoutingHeaderService, RoutingHeaderServiceImpl
 * Related Repository: RoutingHeaderRepository
 * Related Entity    : RoutingHeader
 * Related DTO       : N/A
 * Related Mapper    : RoutingHeaderMapper
 * Related DB Table  : routing_headers
 * Related REST APIs : N/A
 * Depends On        : Inventory Module
 * Used By           : RoutingHeaderRepository, RoutingHeaderMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'routing_headers'. Defines persistent domain object for Manufacturing Module with validation, relationship mappings, and lifecycle callbacks.
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
 * <p><b>Class  :</b> {@code RoutingHeader}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'routing_headers'.</p>
 *
 * <p><b>Database Table   :</b> {@code routing_headers}</p>
 * <p><b>Module Deps      :</b> Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "routing_headers")
public class RoutingHeader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "routing_number", nullable = false, length = 50)
    private String routingNumber;

    @Column(length = 255)
    private String description;

    @Column(nullable = false, length = 20)
    private String revision = "00";

    @Column(nullable = false, length = 30)
    private String status = "DRAFT"; // DRAFT, ACTIVE, SUPERSEDED, OBSOLETE

    @Column(name = "effective_from", nullable = false)
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    private LocalDate effectiveTo;

    @Column(name = "lead_time_hours", precision = 10, scale = 2)
    private BigDecimal leadTimeHours;

    @Column(name = "approved_by")
    private Long approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @OneToMany(mappedBy = "routingHeader", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("operationNumber ASC")
    private java.util.List<RoutingOperation> operations = new java.util.ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    /**
     * Handles the update event or exception in the business workflow.
     *
     */
    @PreUpdate
    protected void onUpdate() { this.updatedAt = LocalDateTime.now(); }

    public RoutingHeader() {}

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
     * Retrieves routing number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRoutingNumber() { return routingNumber; }
    /**
     * Performs the setRoutingNumber operation in this module.
     *
     * @param routingNumber the routingNumber input value
     */
    public void setRoutingNumber(String routingNumber) { this.routingNumber = routingNumber; }
    /**
     * Retrieves description data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDescription() { return description; }
    /**
     * Performs the setDescription operation in this module.
     *
     * @param description the description input value
     */
    public void setDescription(String description) { this.description = description; }
    /**
     * Retrieves revision data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRevision() { return revision; }
    /**
     * Performs the setRevision operation in this module.
     *
     * @param revision the revision input value
     */
    public void setRevision(String revision) { this.revision = revision; }
    /**
     * Retrieves status data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getStatus() { return status; }
    /**
     * Performs the setStatus operation in this module.
     *
     * @param status status filter for narrowing query results
     */
    public void setStatus(String status) { this.status = status; }
    /**
     * Retrieves effective from data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getEffectiveFrom() { return effectiveFrom; }
    /**
     * Performs the setEffectiveFrom operation in this module.
     *
     * @param effectiveFrom the effectiveFrom input value
     */
    public void setEffectiveFrom(LocalDate effectiveFrom) { this.effectiveFrom = effectiveFrom; }
    /**
     * Retrieves effective to data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getEffectiveTo() { return effectiveTo; }
    /**
     * Performs the setEffectiveTo operation in this module.
     *
     * @param effectiveTo the effectiveTo input value
     */
    public void setEffectiveTo(LocalDate effectiveTo) { this.effectiveTo = effectiveTo; }
    /**
     * Retrieves lead time hours data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getLeadTimeHours() { return leadTimeHours; }
    /**
     * Performs the setLeadTimeHours operation in this module.
     *
     * @param leadTimeHours the leadTimeHours input value
     */
    public void setLeadTimeHours(BigDecimal leadTimeHours) { this.leadTimeHours = leadTimeHours; }
    /**
     * Retrieves approved by data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getApprovedBy() { return approvedBy; }
    /**
     * Performs the setApprovedBy operation in this module.
     *
     * @param approvedBy the approvedBy input value
     */
    public void setApprovedBy(Long approvedBy) { this.approvedBy = approvedBy; }
    /**
     * Retrieves approved at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getApprovedAt() { return approvedAt; }
    /**
     * Performs the setApprovedAt operation in this module.
     *
     * @param approvedAt the approvedAt input value
     */
    public void setApprovedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; }
    /**
     * Retrieves operations data from the database.
     *
     * @throws ResourceNotFoundException if the entity is not found
     */
    public java.util.List<RoutingOperation> getOperations() { return operations; }
    /**
     * Performs the setOperations operation in this module.
     *
     * @param operations the operations input value
     */
    public void setOperations(java.util.List<RoutingOperation> operations) { this.operations = operations; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
    /**
     * Retrieves updated at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}