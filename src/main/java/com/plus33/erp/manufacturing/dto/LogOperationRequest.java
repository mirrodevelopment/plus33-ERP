/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.dto
 * File              : LogOperationRequest.java
 * Purpose           : Data Transfer Object for request/response in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: LogOperationController
 * Related Service   : LogOperationService, LogOperationServiceImpl
 * Related Repository: LogOperationRepository
 * Related Entity    : LogOperation
 * Related DTO       : LogOperationRequest
 * Related Mapper    : LogOperationMapper
 * Related DB Table  : log_operations
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : LogOperationController, LogOperationService, LogOperationServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Manufacturing Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.manufacturing.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.*;

public class LogOperationRequest {
    @NotNull private Integer operationSequence;
    @NotBlank private String operationName;
    @NotNull private Long workCenterId;
    @NotNull private String action; // START, COMPLETE, PAUSE
    private BigDecimal completedQuantity;
    private BigDecimal scrappedQuantity;
    private BigDecimal actualSetupHours;
    private BigDecimal actualRunHours;
    private String operatorNotes;
    @NotNull private Long operatorUserId;

    public LogOperationRequest() {}

    /**
     * Retrieves operation sequence data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getOperationSequence() { return operationSequence; }
    /**
     * Performs the setOperationSequence operation in this module.
     *
     * @param operationSequence the operationSequence input value
     */
    public void setOperationSequence(Integer operationSequence) { this.operationSequence = operationSequence; }
    /**
     * Retrieves operation name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getOperationName() { return operationName; }
    /**
     * Performs the setOperationName operation in this module.
     *
     * @param operationName the operationName input value
     */
    public void setOperationName(String operationName) { this.operationName = operationName; }
    /**
     * Retrieves work center id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getWorkCenterId() { return workCenterId; }
    /**
     * Performs the setWorkCenterId operation in this module.
     *
     * @param workCenterId the workCenterId input value
     */
    public void setWorkCenterId(Long workCenterId) { this.workCenterId = workCenterId; }
    /**
     * Retrieves action data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getAction() { return action; }
    /**
     * Performs the setAction operation in this module.
     *
     * @param action the action input value
     */
    public void setAction(String action) { this.action = action; }
    /**
     * Retrieves completed quantity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getCompletedQuantity() { return completedQuantity; }
    /**
     * Performs the setCompletedQuantity operation in this module.
     *
     * @param completedQuantity the completedQuantity input value
     */
    public void setCompletedQuantity(BigDecimal completedQuantity) { this.completedQuantity = completedQuantity; }
    /**
     * Retrieves scrapped quantity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getScrappedQuantity() { return scrappedQuantity; }
    /**
     * Performs the setScrappedQuantity operation in this module.
     *
     * @param scrappedQuantity the scrappedQuantity input value
     */
    public void setScrappedQuantity(BigDecimal scrappedQuantity) { this.scrappedQuantity = scrappedQuantity; }
    /**
     * Retrieves actual setup hours data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getActualSetupHours() { return actualSetupHours; }
    /**
     * Performs the setActualSetupHours operation in this module.
     *
     * @param actualSetupHours the actualSetupHours input value
     */
    public void setActualSetupHours(BigDecimal actualSetupHours) { this.actualSetupHours = actualSetupHours; }
    /**
     * Retrieves actual run hours data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getActualRunHours() { return actualRunHours; }
    /**
     * Performs the setActualRunHours operation in this module.
     *
     * @param actualRunHours the actualRunHours input value
     */
    public void setActualRunHours(BigDecimal actualRunHours) { this.actualRunHours = actualRunHours; }
    /**
     * Retrieves operator notes data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getOperatorNotes() { return operatorNotes; }
    /**
     * Performs the setOperatorNotes operation in this module.
     *
     * @param operatorNotes the operatorNotes input value
     */
    public void setOperatorNotes(String operatorNotes) { this.operatorNotes = operatorNotes; }
    /**
     * Retrieves operator user id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getOperatorUserId() { return operatorUserId; }
    /**
     * Performs the setOperatorUserId operation in this module.
     *
     * @param operatorUserId the operatorUserId input value
     */
    public void setOperatorUserId(Long operatorUserId) { this.operatorUserId = operatorUserId; }
}
