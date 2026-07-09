/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.dto
 * File              : CreateQualityInspectionRequest.java
 * Purpose           : Data Transfer Object for request/response in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CreateQualityInspectionController
 * Related Service   : CreateQualityInspectionService, CreateQualityInspectionServiceImpl
 * Related Repository: CreateQualityInspectionRepository
 * Related Entity    : CreateQualityInspection
 * Related DTO       : CreateQualityInspectionRequest
 * Related Mapper    : CreateQualityInspectionMapper
 * Related DB Table  : create_quality_inspections
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CreateQualityInspectionController, CreateQualityInspectionService, CreateQualityInspectionServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Manufacturing Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.manufacturing.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.constraints.*;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code CreateQualityInspectionRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.dto}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Manufacturing Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public class CreateQualityInspectionRequest {
    @NotNull private Long companyId;
    @NotBlank private String inspectionNumber;
    private Long productionOrderId;
    @NotNull private Long productId;
    @NotBlank private String inspectionType;
    @NotNull @Positive private BigDecimal sampleSize;
    private LocalDate inspectionDate;
    private Long inspectorUserId;
    private String notes;

    public CreateQualityInspectionRequest() {}

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
     * Retrieves inspection date data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getInspectionDate() { return inspectionDate; }
    /**
     * Performs the setInspectionDate operation in this module.
     *
     * @param inspectionDate the inspectionDate input value
     */
    public void setInspectionDate(LocalDate inspectionDate) { this.inspectionDate = inspectionDate; }
    /**
     * Retrieves inspector user id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getInspectorUserId() { return inspectorUserId; }
    /**
     * Performs the setInspectorUserId operation in this module.
     *
     * @param inspectorUserId the inspectorUserId input value
     */
    public void setInspectorUserId(Long inspectorUserId) { this.inspectorUserId = inspectorUserId; }
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
}
