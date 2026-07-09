/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.entity
 * File              : QualityInspection.java
 * Purpose           : JPA Entity representing a persistent database record in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: QualityInspectionController
 * Related Service   : QualityInspectionService, QualityInspectionServiceImpl
 * Related Repository: QualityInspectionRepository
 * Related Entity    : QualityInspection
 * Related DTO       : N/A
 * Related Mapper    : QualityInspectionMapper
 * Related DB Table  : quality_inspections
 * Related REST APIs : N/A
 * Depends On        : Inventory Module
 * Used By           : QualityInspectionRepository, QualityInspectionMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'quality_inspections'. Defines persistent domain object for Manufacturing Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.manufacturing.entity;

import com.plus33.erp.inventory.entity.Product;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code QualityInspection}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'quality_inspections'.</p>
 *
 * <p><b>Database Table   :</b> {@code quality_inspections}</p>
 * <p><b>Module Deps      :</b> Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "quality_inspections")
public class QualityInspection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "production_order_id")
    private ProductionOrder productionOrder;

    @Column(name = "inspection_number", nullable = false, length = 50)
    private String inspectionNumber;

    @Column(name = "inspection_type", nullable = false, length = 30)
    private String inspectionType; // INCOMING, IN_PROCESS, FINAL, RECEIVING

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private InspectionStatus status = InspectionStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "lot_number", length = 100)
    private String lotNumber;

    @Column(name = "serial_number", length = 100)
    private String serialNumber;

    @Column(name = "inspected_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal inspectedQuantity = BigDecimal.ZERO;

    @Column(name = "passed_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal passedQuantity = BigDecimal.ZERO;

    @Column(name = "failed_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal failedQuantity = BigDecimal.ZERO;

    @Column(name = "sample_size", precision = 18, scale = 6)
    private BigDecimal sampleSize;

    @Column(name = "sampling_plan", length = 100)
    private String samplingPlan;

    @Column(name = "disposition", length = 30)
    private String disposition; // ACCEPT, REJECT, REWORK, CONDITIONAL_ACCEPT

    @Column(name = "hold_production", nullable = false)
    private Boolean holdProduction = false;

    @Column(name = "non_conformance_report", columnDefinition = "TEXT")
    private String nonConformanceReport;

    @Column(name = "corrective_action", columnDefinition = "TEXT")
    private String correctiveAction;

    @Column(name = "inspected_by")
    private Long inspectedBy;

    @Column(name = "approved_by")
    private Long approvedBy;

    @Column(name = "inspected_at")
    private LocalDateTime inspectedAt;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

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

    public QualityInspection() {}

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
     * Retrieves production order data from the database.
     *
     * @return the ProductionOrder result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public ProductionOrder getProductionOrder() { return productionOrder; }
    /**
     * Performs the setProductionOrder operation in this module.
     *
     * @param productionOrder the productionOrder input value
     */
    public void setProductionOrder(ProductionOrder productionOrder) { this.productionOrder = productionOrder; }
    /**
     * Retrieves inspection number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getInspectionNumber() { return inspectionNumber; }
    /**
     * Performs the setInspectionNumber operation in this module.
     *
     * @param inspectionNumber the inspectionNumber input value
     */
    public void setInspectionNumber(String inspectionNumber) { this.inspectionNumber = inspectionNumber; }
    /**
     * Retrieves inspection type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getInspectionType() { return inspectionType; }
    /**
     * Performs the setInspectionType operation in this module.
     *
     * @param inspectionType the inspectionType input value
     */
    public void setInspectionType(String inspectionType) { this.inspectionType = inspectionType; }
    /**
     * Retrieves status data from the database.
     *
     * @return the InspectionStatus result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public InspectionStatus getStatus() { return status; }
    /**
     * Performs the setStatus operation in this module.
     *
     * @param status status filter for narrowing query results
     */
    public void setStatus(InspectionStatus status) { this.status = status; }
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
     * Retrieves lot number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getLotNumber() { return lotNumber; }
    /**
     * Performs the setLotNumber operation in this module.
     *
     * @param lotNumber the lotNumber input value
     */
    public void setLotNumber(String lotNumber) { this.lotNumber = lotNumber; }
    /**
     * Retrieves serial number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSerialNumber() { return serialNumber; }
    /**
     * Performs the setSerialNumber operation in this module.
     *
     * @param serialNumber the serialNumber input value
     */
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
    /**
     * Retrieves inspected quantity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getInspectedQuantity() { return inspectedQuantity; }
    /**
     * Performs the setInspectedQuantity operation in this module.
     *
     * @param inspectedQuantity the inspectedQuantity input value
     */
    public void setInspectedQuantity(BigDecimal inspectedQuantity) { this.inspectedQuantity = inspectedQuantity; }
    /**
     * Retrieves passed quantity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getPassedQuantity() { return passedQuantity; }
    /**
     * Performs the setPassedQuantity operation in this module.
     *
     * @param passedQuantity the passedQuantity input value
     */
    public void setPassedQuantity(BigDecimal passedQuantity) { this.passedQuantity = passedQuantity; }
    /**
     * Retrieves failed quantity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getFailedQuantity() { return failedQuantity; }
    /**
     * Performs the setFailedQuantity operation in this module.
     *
     * @param failedQuantity the failedQuantity input value
     */
    public void setFailedQuantity(BigDecimal failedQuantity) { this.failedQuantity = failedQuantity; }
    /**
     * Retrieves sample size data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getSampleSize() { return sampleSize; }
    /**
     * Performs the setSampleSize operation in this module.
     *
     * @param sampleSize the sampleSize input value
     */
    public void setSampleSize(BigDecimal sampleSize) { this.sampleSize = sampleSize; }
    /**
     * Retrieves sampling plan data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSamplingPlan() { return samplingPlan; }
    /**
     * Performs the setSamplingPlan operation in this module.
     *
     * @param samplingPlan the samplingPlan input value
     */
    public void setSamplingPlan(String samplingPlan) { this.samplingPlan = samplingPlan; }
    /**
     * Retrieves disposition data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDisposition() { return disposition; }
    /**
     * Performs the setDisposition operation in this module.
     *
     * @param disposition the disposition input value
     */
    public void setDisposition(String disposition) { this.disposition = disposition; }
    /**
     * Retrieves hold production data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getHoldProduction() { return holdProduction; }
    /**
     * Performs the setHoldProduction operation in this module.
     *
     * @param holdProduction the holdProduction input value
     */
    public void setHoldProduction(Boolean holdProduction) { this.holdProduction = holdProduction; }
    /**
     * Retrieves non conformance report data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getNonConformanceReport() { return nonConformanceReport; }
    /**
     * Performs the setNonConformanceReport operation in this module.
     *
     * @param nonConformanceReport the nonConformanceReport input value
     */
    public void setNonConformanceReport(String nonConformanceReport) { this.nonConformanceReport = nonConformanceReport; }
    /**
     * Retrieves corrective action data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCorrectiveAction() { return correctiveAction; }
    /**
     * Performs the setCorrectiveAction operation in this module.
     *
     * @param correctiveAction the correctiveAction input value
     */
    public void setCorrectiveAction(String correctiveAction) { this.correctiveAction = correctiveAction; }
    /**
     * Retrieves inspected by data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getInspectedBy() { return inspectedBy; }
    /**
     * Performs the setInspectedBy operation in this module.
     *
     * @param inspectedBy the inspectedBy input value
     */
    public void setInspectedBy(Long inspectedBy) { this.inspectedBy = inspectedBy; }
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
     * Retrieves inspected at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getInspectedAt() { return inspectedAt; }
    /**
     * Performs the setInspectedAt operation in this module.
     *
     * @param inspectedAt the inspectedAt input value
     */
    public void setInspectedAt(LocalDateTime inspectedAt) { this.inspectedAt = inspectedAt; }
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
     * Retrieves notes data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getNotes() { return notes; }
    /**
     * Performs the setNotes operation in this module.
     *
     * @param notes the notes input value
     */
    public void setNotes(String notes) { this.notes = notes; }
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