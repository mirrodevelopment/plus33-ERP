/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.dto
 * File              : ExecuteMrpRunRequest.java
 * Purpose           : Data Transfer Object for request/response in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ExecuteMrpRunController
 * Related Service   : ExecuteMrpRunService, ExecuteMrpRunServiceImpl
 * Related Repository: ExecuteMrpRunRepository
 * Related Entity    : ExecuteMrpRun
 * Related DTO       : ExecuteMrpRunRequest
 * Related Mapper    : ExecuteMrpRunMapper
 * Related DB Table  : execute_mrp_runs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ExecuteMrpRunController, ExecuteMrpRunService, ExecuteMrpRunServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Manufacturing Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.manufacturing.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.*;

public class ExecuteMrpRunRequest {
    @NotNull private Long companyId;
    @NotBlank private String runNumber;
    @NotNull private LocalDate horizonStartDate;
    @NotNull private LocalDate horizonEndDate;
    @NotNull private Long initiatedBy;

    public ExecuteMrpRunRequest() {}

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
     * Retrieves run number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRunNumber() { return runNumber; }
    /**
     * Performs the setRunNumber operation in this module.
     *
     * @param runNumber the runNumber input value
     */
    public void setRunNumber(String runNumber) { this.runNumber = runNumber; }
    /**
     * Retrieves horizon start date data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getHorizonStartDate() { return horizonStartDate; }
    /**
     * Performs the setHorizonStartDate operation in this module.
     *
     * @param horizonStartDate the horizonStartDate input value
     */
    public void setHorizonStartDate(LocalDate horizonStartDate) { this.horizonStartDate = horizonStartDate; }
    /**
     * Retrieves horizon end date data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getHorizonEndDate() { return horizonEndDate; }
    /**
     * Performs the setHorizonEndDate operation in this module.
     *
     * @param horizonEndDate the horizonEndDate input value
     */
    public void setHorizonEndDate(LocalDate horizonEndDate) { this.horizonEndDate = horizonEndDate; }
    /**
     * Retrieves initiated by data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getInitiatedBy() { return initiatedBy; }
    /**
     * Performs the setInitiatedBy operation in this module.
     *
     * @param initiatedBy the initiatedBy input value
     */
    public void setInitiatedBy(Long initiatedBy) { this.initiatedBy = initiatedBy; }
}
