/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.dto
 * File              : QualityInspectionDto.java
 * Purpose           : Data Transfer Object for request/response in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: QualityInspectionDtoController
 * Related Service   : QualityInspectionDtoService, QualityInspectionDtoServiceImpl
 * Related Repository: QualityInspectionDtoRepository
 * Related Entity    : QualityInspectionDto
 * Related DTO       : QualityInspectionDto
 * Related Mapper    : QualityInspectionDtoMapper
 * Related DB Table  : quality_inspection_dtos
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : QualityInspectionDtoController, QualityInspectionDtoService, QualityInspectionDtoServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Manufacturing Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.manufacturing.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class QualityInspectionDto {
    private Long id;
    private Long companyId;
    private String inspectionNumber;
    private Long productionOrderId;
    private String productionOrderNumber;
    private Long productId;
    private String productCode;
    private String productName;
    private String inspectionType;
    private String status;
    private BigDecimal sampleSize;
    private BigDecimal inspectedQuantity;
    private BigDecimal passedQuantity;
    private BigDecimal failedQuantity;
    private String samplingPlan;
    private String disposition;
    private Boolean holdProduction;
    private String nonConformanceReport;
    private String correctiveAction;
    private Long inspectedBy;
    private Long approvedBy;
    private LocalDateTime inspectedAt;
    private LocalDateTime approvedAt;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public QualityInspectionDto() {}

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
     * Retrieves production order id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getProductionOrderId() { return productionOrderId; }
    /**
     * Performs the setProductionOrderId operation in this module.
     *
     * @param productionOrderId the productionOrderId input value
     */
    public void setProductionOrderId(Long productionOrderId) { this.productionOrderId = productionOrderId; }
    /**
     * Retrieves production order number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getProductionOrderNumber() { return productionOrderNumber; }
    /**
     * Performs the setProductionOrderNumber operation in this module.
     *
     * @param productionOrderNumber the productionOrderNumber input value
     */
    public void setProductionOrderNumber(String productionOrderNumber) { this.productionOrderNumber = productionOrderNumber; }
    /**
     * Retrieves product id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getProductId() { return productId; }
    /**
     * Performs the setProductId operation in this module.
     *
     * @param productId the productId input value
     */
    public void setProductId(Long productId) { this.productId = productId; }
    /**
     * Retrieves product code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getProductCode() { return productCode; }
    /**
     * Performs the setProductCode operation in this module.
     *
     * @param productCode the productCode input value
     */
    public void setProductCode(String productCode) { this.productCode = productCode; }
    /**
     * Retrieves product name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getProductName() { return productName; }
    /**
     * Performs the setProductName operation in this module.
     *
     * @param productName the productName input value
     */
    public void setProductName(String productName) { this.productName = productName; }
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
     * Performs the setCreatedAt operation in this module.
     *
     * @param createdAt the createdAt input value
     */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    /**
     * Retrieves updated at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    /**
     * Performs the setUpdatedAt operation in this module.
     *
     * @param updatedAt the updatedAt input value
     */
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
