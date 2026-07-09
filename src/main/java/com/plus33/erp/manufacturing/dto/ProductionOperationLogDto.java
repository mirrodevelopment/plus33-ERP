/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.dto
 * File              : ProductionOperationLogDto.java
 * Purpose           : Data Transfer Object for request/response in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProductionOperationLogDtoController
 * Related Service   : ProductionOperationLogDtoService, ProductionOperationLogDtoServiceImpl
 * Related Repository: ProductionOperationLogDtoRepository
 * Related Entity    : ProductionOperationLogDto
 * Related DTO       : ProductionOperationLogDto
 * Related Mapper    : ProductionOperationLogDtoMapper
 * Related DB Table  : production_operation_log_dtos
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProductionOperationLogDtoController, ProductionOperationLogDtoService, ProductionOperationLogDtoServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Manufacturing Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.manufacturing.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductionOperationLogDto {
    private Long id;
    private Long productionOrderId;
    private Integer operationSequence;
    private String operationName;
    private Long workCenterId;
    private String workCenterCode;
    private String status;
    private BigDecimal setupHoursPlanned;
    private BigDecimal setupHoursActual;
    private BigDecimal runHoursPlanned;
    private BigDecimal runHoursActual;
    private BigDecimal completedQuantity;
    private BigDecimal scrappedQuantity;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long operatorUserId;
    private String operatorNotes;

    public ProductionOperationLogDto() {}

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
     * Retrieves work center code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getWorkCenterCode() { return workCenterCode; }
    /**
     * Performs the setWorkCenterCode operation in this module.
     *
     * @param workCenterCode the workCenterCode input value
     */
    public void setWorkCenterCode(String workCenterCode) { this.workCenterCode = workCenterCode; }
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
     * Retrieves setup hours planned data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getSetupHoursPlanned() { return setupHoursPlanned; }
    /**
     * Performs the setSetupHoursPlanned operation in this module.
     *
     * @param setupHoursPlanned the setupHoursPlanned input value
     */
    public void setSetupHoursPlanned(BigDecimal setupHoursPlanned) { this.setupHoursPlanned = setupHoursPlanned; }
    /**
     * Retrieves setup hours actual data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getSetupHoursActual() { return setupHoursActual; }
    /**
     * Performs the setSetupHoursActual operation in this module.
     *
     * @param setupHoursActual the setupHoursActual input value
     */
    public void setSetupHoursActual(BigDecimal setupHoursActual) { this.setupHoursActual = setupHoursActual; }
    /**
     * Retrieves run hours planned data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getRunHoursPlanned() { return runHoursPlanned; }
    /**
     * Performs the setRunHoursPlanned operation in this module.
     *
     * @param runHoursPlanned the runHoursPlanned input value
     */
    public void setRunHoursPlanned(BigDecimal runHoursPlanned) { this.runHoursPlanned = runHoursPlanned; }
    /**
     * Retrieves run hours actual data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getRunHoursActual() { return runHoursActual; }
    /**
     * Performs the setRunHoursActual operation in this module.
     *
     * @param runHoursActual the runHoursActual input value
     */
    public void setRunHoursActual(BigDecimal runHoursActual) { this.runHoursActual = runHoursActual; }
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
     * Retrieves start time data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getStartTime() { return startTime; }
    /**
     * Performs the setStartTime operation in this module.
     *
     * @param startTime the startTime input value
     */
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    /**
     * Retrieves end time data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getEndTime() { return endTime; }
    /**
     * Performs the setEndTime operation in this module.
     *
     * @param endTime the endTime input value
     */
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
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
}
