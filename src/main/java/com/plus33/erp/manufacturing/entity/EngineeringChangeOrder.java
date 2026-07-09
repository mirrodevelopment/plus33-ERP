/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.entity
 * File              : EngineeringChangeOrder.java
 * Purpose           : JPA Entity representing a persistent database record in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EngineeringChangeOrderController
 * Related Service   : EngineeringChangeOrderService, EngineeringChangeOrderServiceImpl
 * Related Repository: EngineeringChangeOrderRepository
 * Related Entity    : EngineeringChangeOrder
 * Related DTO       : N/A
 * Related Mapper    : EngineeringChangeOrderMapper
 * Related DB Table  : engineering_change_orders
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : EngineeringChangeOrderRepository, EngineeringChangeOrderMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'engineering_change_orders'. Defines persistent domain object for Manufacturing Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.manufacturing.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code EngineeringChangeOrder}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'engineering_change_orders'.</p>
 *
 * <p><b>Database Table   :</b> {@code engineering_change_orders}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "engineering_change_orders")
public class EngineeringChangeOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "eco_number", nullable = false, length = 50)
    private String ecoNumber;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 255)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EcoStatus status = EcoStatus.DRAFT;

    @Column(nullable = false, length = 20)
    private String priority = "NORMAL"; // LOW, NORMAL, HIGH, CRITICAL, SAFETY

    @Column(name = "effective_date")
    private java.time.LocalDate effectiveDate;

    @Column(name = "requested_by", nullable = false)
    private Long requestedBy;

    @Column(name = "reviewed_by")
    private Long reviewedBy;

    @Column(name = "approved_by")
    private Long approvedBy;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "implemented_at")
    private LocalDateTime implementedAt;

    @OneToMany(mappedBy = "engineeringChangeOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<EngineeringChangeLine> lines = new java.util.ArrayList<>();

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

    public EngineeringChangeOrder() {}

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
     * Retrieves eco number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getEcoNumber() { return ecoNumber; }
    /**
     * Performs the setEcoNumber operation in this module.
     *
     * @param ecoNumber the ecoNumber input value
     */
    public void setEcoNumber(String ecoNumber) { this.ecoNumber = ecoNumber; }
    /**
     * Retrieves title data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTitle() { return title; }
    /**
     * Performs the setTitle operation in this module.
     *
     * @param title the title input value
     */
    public void setTitle(String title) { this.title = title; }
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
     * Retrieves reason data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getReason() { return reason; }
    /**
     * Performs the setReason operation in this module.
     *
     * @param reason the reason input value
     */
    public void setReason(String reason) { this.reason = reason; }
    /**
     * Retrieves status data from the database.
     *
     * @return the EcoStatus result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public EcoStatus getStatus() { return status; }
    /**
     * Performs the setStatus operation in this module.
     *
     * @param status status filter for narrowing query results
     */
    public void setStatus(EcoStatus status) { this.status = status; }
    /**
     * Retrieves priority data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPriority() { return priority; }
    /**
     * Performs the setPriority operation in this module.
     *
     * @param priority the priority input value
     */
    public void setPriority(String priority) { this.priority = priority; }
    /**
     * Retrieves effective date data from the database.
     *
     * @throws ResourceNotFoundException if the entity is not found
     */
    public java.time.LocalDate getEffectiveDate() { return effectiveDate; }
    /**
     * Performs the setEffectiveDate operation in this module.
     *
     * @param effectiveDate the effectiveDate input value
     */
    public void setEffectiveDate(java.time.LocalDate effectiveDate) { this.effectiveDate = effectiveDate; }
    /**
     * Retrieves requested by data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getRequestedBy() { return requestedBy; }
    /**
     * Performs the setRequestedBy operation in this module.
     *
     * @param requestedBy the requestedBy input value
     */
    public void setRequestedBy(Long requestedBy) { this.requestedBy = requestedBy; }
    /**
     * Retrieves reviewed by data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getReviewedBy() { return reviewedBy; }
    /**
     * Performs the setReviewedBy operation in this module.
     *
     * @param reviewedBy the reviewedBy input value
     */
    public void setReviewedBy(Long reviewedBy) { this.reviewedBy = reviewedBy; }
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
     * Retrieves reviewed at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getReviewedAt() { return reviewedAt; }
    /**
     * Performs the setReviewedAt operation in this module.
     *
     * @param reviewedAt the reviewedAt input value
     */
    public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }
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
     * Retrieves implemented at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getImplementedAt() { return implementedAt; }
    /**
     * Performs the setImplementedAt operation in this module.
     *
     * @param implementedAt the implementedAt input value
     */
    public void setImplementedAt(LocalDateTime implementedAt) { this.implementedAt = implementedAt; }
    /**
     * Retrieves lines data from the database.
     *
     * @throws ResourceNotFoundException if the entity is not found
     */
    public java.util.List<EngineeringChangeLine> getLines() { return lines; }
    /**
     * Performs the setLines operation in this module.
     *
     * @param lines the lines input value
     */
    public void setLines(java.util.List<EngineeringChangeLine> lines) { this.lines = lines; }
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