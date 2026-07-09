/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.dto
 * File              : MrpRunDto.java
 * Purpose           : Data Transfer Object for request/response in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: MrpRunDtoController
 * Related Service   : MrpRunDtoService, MrpRunDtoServiceImpl
 * Related Repository: MrpRunDtoRepository
 * Related Entity    : MrpRunDto
 * Related DTO       : MrpRunDto
 * Related Mapper    : MrpRunDtoMapper
 * Related DB Table  : mrp_run_dtos
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : MrpRunDtoController, MrpRunDtoService, MrpRunDtoServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Manufacturing Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.manufacturing.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MrpRunDto {
    private Long id;
    private Long companyId;
    private String runNumber;
    private LocalDate horizonStartDate;
    private LocalDate horizonEndDate;
    private String status;
    private Integer itemsProcessed;
    private Integer ordersGenerated;
    private Long initiatedBy;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private String executionLog;

    public MrpRunDto() {}

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
     * Retrieves items processed data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getItemsProcessed() { return itemsProcessed; }
    /**
     * Performs the setItemsProcessed operation in this module.
     *
     * @param itemsProcessed the itemsProcessed input value
     */
    public void setItemsProcessed(Integer itemsProcessed) { this.itemsProcessed = itemsProcessed; }
    /**
     * Retrieves orders generated data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getOrdersGenerated() { return ordersGenerated; }
    /**
     * Performs the setOrdersGenerated operation in this module.
     *
     * @param ordersGenerated the ordersGenerated input value
     */
    public void setOrdersGenerated(Integer ordersGenerated) { this.ordersGenerated = ordersGenerated; }
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
    /**
     * Retrieves started at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getStartedAt() { return startedAt; }
    /**
     * Performs the setStartedAt operation in this module.
     *
     * @param startedAt the startedAt input value
     */
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
    /**
     * Retrieves completed at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCompletedAt() { return completedAt; }
    /**
     * Performs the setCompletedAt operation in this module.
     *
     * @param completedAt the completedAt input value
     */
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    /**
     * Retrieves execution log data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getExecutionLog() { return executionLog; }
    /**
     * Performs the setExecutionLog operation in this module.
     *
     * @param executionLog the executionLog input value
     */
    public void setExecutionLog(String executionLog) { this.executionLog = executionLog; }
}
