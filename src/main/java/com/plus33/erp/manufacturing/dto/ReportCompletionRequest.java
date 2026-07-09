/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.dto
 * File              : ReportCompletionRequest.java
 * Purpose           : Data Transfer Object for request/response in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ReportCompletionController
 * Related Service   : ReportCompletionService, ReportCompletionServiceImpl
 * Related Repository: ReportCompletionRepository
 * Related Entity    : ReportCompletion
 * Related DTO       : ReportCompletionRequest
 * Related Mapper    : ReportCompletionMapper
 * Related DB Table  : report_completions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ReportCompletionController, ReportCompletionService, ReportCompletionServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Manufacturing Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.manufacturing.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.*;

public class ReportCompletionRequest {
    @NotNull @Positive private BigDecimal producedQuantity;
    private BigDecimal rejectedQuantity = BigDecimal.ZERO;
    private Long targetWarehouseId;
    private String notes;
    @NotNull private Long reportedBy;

    public ReportCompletionRequest() {}

    /**
     * Retrieves produced quantity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getProducedQuantity() { return producedQuantity; }
    /**
     * Performs the setProducedQuantity operation in this module.
     *
     * @param producedQuantity the producedQuantity input value
     */
    public void setProducedQuantity(BigDecimal producedQuantity) { this.producedQuantity = producedQuantity; }
    /**
     * Retrieves rejected quantity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getRejectedQuantity() { return rejectedQuantity; }
    /**
     * Performs the setRejectedQuantity operation in this module.
     *
     * @param rejectedQuantity the rejectedQuantity input value
     */
    public void setRejectedQuantity(BigDecimal rejectedQuantity) { this.rejectedQuantity = rejectedQuantity; }
    /**
     * Retrieves target warehouse id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getTargetWarehouseId() { return targetWarehouseId; }
    /**
     * Performs the setTargetWarehouseId operation in this module.
     *
     * @param targetWarehouseId the targetWarehouseId input value
     */
    public void setTargetWarehouseId(Long targetWarehouseId) { this.targetWarehouseId = targetWarehouseId; }
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
     * Retrieves reported by data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getReportedBy() { return reportedBy; }
    /**
     * Performs the setReportedBy operation in this module.
     *
     * @param reportedBy the reportedBy input value
     */
    public void setReportedBy(Long reportedBy) { this.reportedBy = reportedBy; }
}
