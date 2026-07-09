/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.dto
 * File              : CreateProductionOrderOperationRequest.java
 * Purpose           : Data Transfer Object for request/response in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CreateProductionOrderOperationController
 * Related Service   : CreateProductionOrderOperationService, CreateProductionOrderOperationServiceImpl
 * Related Repository: CreateProductionOrderOperationRepository
 * Related Entity    : CreateProductionOrderOperation
 * Related DTO       : CreateProductionOrderOperationRequest
 * Related Mapper    : CreateProductionOrderOperationMapper
 * Related DB Table  : create_production_order_operations
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CreateProductionOrderOperationController, CreateProductionOrderOperationService, CreateProductionOrderOperationServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Manufacturing Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.manufacturing.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.*;

public class CreateProductionOrderOperationRequest {
    @NotNull private Integer operationNumber;
    private String operationCode;
    private String operationName;
    @NotNull private Long workCenterId;
    private Long machineId;
    @NotNull @PositiveOrZero private BigDecimal estimatedSetupHours;
    @NotNull @PositiveOrZero private BigDecimal estimatedRunHours;
    private String description;

    public CreateProductionOrderOperationRequest() {}

    /**
     * Retrieves operation number data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getOperationNumber() { return operationNumber; }
    /**
     * Performs the setOperationNumber operation in this module.
     *
     * @param operationNumber the operationNumber input value
     */
    public void setOperationNumber(Integer operationNumber) { this.operationNumber = operationNumber; }
    /**
     * Retrieves operation code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getOperationCode() { return operationCode; }
    /**
     * Performs the setOperationCode operation in this module.
     *
     * @param operationCode the operationCode input value
     */
    public void setOperationCode(String operationCode) { this.operationCode = operationCode; }
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
     * Retrieves machine id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getMachineId() { return machineId; }
    /**
     * Performs the setMachineId operation in this module.
     *
     * @param machineId the machineId input value
     */
    public void setMachineId(Long machineId) { this.machineId = machineId; }
    /**
     * Retrieves estimated setup hours data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getEstimatedSetupHours() { return estimatedSetupHours; }
    /**
     * Performs the setEstimatedSetupHours operation in this module.
     *
     * @param estimatedSetupHours the estimatedSetupHours input value
     */
    public void setEstimatedSetupHours(BigDecimal estimatedSetupHours) { this.estimatedSetupHours = estimatedSetupHours; }
    /**
     * Retrieves estimated run hours data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getEstimatedRunHours() { return estimatedRunHours; }
    /**
     * Performs the setEstimatedRunHours operation in this module.
     *
     * @param estimatedRunHours the estimatedRunHours input value
     */
    public void setEstimatedRunHours(BigDecimal estimatedRunHours) { this.estimatedRunHours = estimatedRunHours; }
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
}
