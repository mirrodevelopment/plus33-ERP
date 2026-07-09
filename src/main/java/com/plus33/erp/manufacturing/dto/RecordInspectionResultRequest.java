/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.dto
 * File              : RecordInspectionResultRequest.java
 * Purpose           : Data Transfer Object for request/response in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: RecordInspectionResultController
 * Related Service   : RecordInspectionResultService, RecordInspectionResultServiceImpl
 * Related Repository: RecordInspectionResultRepository
 * Related Entity    : RecordInspectionResult
 * Related DTO       : RecordInspectionResultRequest
 * Related Mapper    : RecordInspectionResultMapper
 * Related DB Table  : record_inspection_results
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : RecordInspectionResultController, RecordInspectionResultService, RecordInspectionResultServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Manufacturing Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.manufacturing.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.*;

public class RecordInspectionResultRequest {
    @NotNull private BigDecimal passedQuantity;
    @NotNull private BigDecimal failedQuantity;
    private String defectCode;
    private String defectDescription;
    private String disposition; // ACCEPT, REJECT, REWORK, SCRAP
    private String notes;
    @NotNull private Long inspectorUserId;

    public RecordInspectionResultRequest() {}

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
     * Retrieves defect code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDefectCode() { return defectCode; }
    /**
     * Performs the setDefectCode operation in this module.
     *
     * @param defectCode the defectCode input value
     */
    public void setDefectCode(String defectCode) { this.defectCode = defectCode; }
    /**
     * Retrieves defect description data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDefectDescription() { return defectDescription; }
    /**
     * Performs the setDefectDescription operation in this module.
     *
     * @param defectDescription the defectDescription input value
     */
    public void setDefectDescription(String defectDescription) { this.defectDescription = defectDescription; }
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
}
