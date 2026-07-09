/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.dto
 * File              : CompleteOperationRequest.java
 * Purpose           : Data Transfer Object for request/response in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CompleteOperationController
 * Related Service   : CompleteOperationService, CompleteOperationServiceImpl
 * Related Repository: CompleteOperationRepository
 * Related Entity    : CompleteOperation
 * Related DTO       : CompleteOperationRequest
 * Related Mapper    : CompleteOperationMapper
 * Related DB Table  : complete_operations
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CompleteOperationController, CompleteOperationService, CompleteOperationServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Manufacturing Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.manufacturing.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.*;

public class CompleteOperationRequest {
    @NotNull @PositiveOrZero private BigDecimal actualSetupHours;
    @NotNull @PositiveOrZero private BigDecimal actualRunHours;
    @NotNull @PositiveOrZero private BigDecimal yieldQuantity;
    @NotNull @PositiveOrZero private BigDecimal scrapQuantity;
    private Long machineId;
    private Long laborGroupId;
    private String notes;

    public CompleteOperationRequest() {}

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
     * Retrieves yield quantity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getYieldQuantity() { return yieldQuantity; }
    /**
     * Performs the setYieldQuantity operation in this module.
     *
     * @param yieldQuantity the yieldQuantity input value
     */
    public void setYieldQuantity(BigDecimal yieldQuantity) { this.yieldQuantity = yieldQuantity; }
    /**
     * Retrieves scrap quantity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getScrapQuantity() { return scrapQuantity; }
    /**
     * Performs the setScrapQuantity operation in this module.
     *
     * @param scrapQuantity the scrapQuantity input value
     */
    public void setScrapQuantity(BigDecimal scrapQuantity) { this.scrapQuantity = scrapQuantity; }
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
     * Retrieves labor group id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getLaborGroupId() { return laborGroupId; }
    /**
     * Performs the setLaborGroupId operation in this module.
     *
     * @param laborGroupId the laborGroupId input value
     */
    public void setLaborGroupId(Long laborGroupId) { this.laborGroupId = laborGroupId; }
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
