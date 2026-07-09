/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.dto
 * File              : ProductionOrderOperationDto.java
 * Purpose           : Data Transfer Object for request/response in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProductionOrderOperationDtoController
 * Related Service   : ProductionOrderOperationDtoService, ProductionOrderOperationDtoServiceImpl
 * Related Repository: ProductionOrderOperationDtoRepository
 * Related Entity    : ProductionOrderOperationDto
 * Related DTO       : ProductionOrderOperationDto
 * Related Mapper    : ProductionOrderOperationDtoMapper
 * Related DB Table  : production_order_operation_dtos
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProductionOrderOperationDtoController, ProductionOrderOperationDtoService, ProductionOrderOperationDtoServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Manufacturing Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.manufacturing.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductionOrderOperationDto {
    private Long id;
    private Long productionOrderId;
    private Long routingOperationId;
    private Integer operationNumber;
    private String operationCode;
    private String description;
    private Long workCenterId;
    private String workCenterCode;
    private String workCenterName;
    private Long machineId;
    private String machineCode;
    private String status;
    private BigDecimal plannedQuantity;
    private BigDecimal completedQuantity;
    private BigDecimal scrappedQuantity;
    private BigDecimal plannedSetupHours;
    private BigDecimal actualSetupHours;
    private BigDecimal plannedRunHours;
    private BigDecimal actualRunHours;
    private LocalDateTime plannedStartDatetime;
    private LocalDateTime plannedEndDatetime;
    private LocalDateTime actualStartDatetime;
    private LocalDateTime actualEndDatetime;

    public ProductionOrderOperationDto() {}

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
     * Retrieves routing operation id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getRoutingOperationId() { return routingOperationId; }
    /**
     * Performs the setRoutingOperationId operation in this module.
     *
     * @param routingOperationId the routingOperationId input value
     */
    public void setRoutingOperationId(Long routingOperationId) { this.routingOperationId = routingOperationId; }
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
     * Retrieves work center name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getWorkCenterName() { return workCenterName; }
    /**
     * Performs the setWorkCenterName operation in this module.
     *
     * @param workCenterName the workCenterName input value
     */
    public void setWorkCenterName(String workCenterName) { this.workCenterName = workCenterName; }
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
     * Retrieves machine code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getMachineCode() { return machineCode; }
    /**
     * Performs the setMachineCode operation in this module.
     *
     * @param machineCode the machineCode input value
     */
    public void setMachineCode(String machineCode) { this.machineCode = machineCode; }
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
     * Retrieves planned quantity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getPlannedQuantity() { return plannedQuantity; }
    /**
     * Performs the setPlannedQuantity operation in this module.
     *
     * @param plannedQuantity the plannedQuantity input value
     */
    public void setPlannedQuantity(BigDecimal plannedQuantity) { this.plannedQuantity = plannedQuantity; }
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
     * Retrieves planned setup hours data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getPlannedSetupHours() { return plannedSetupHours; }
    /**
     * Performs the setPlannedSetupHours operation in this module.
     *
     * @param plannedSetupHours the plannedSetupHours input value
     */
    public void setPlannedSetupHours(BigDecimal plannedSetupHours) { this.plannedSetupHours = plannedSetupHours; }
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
     * Retrieves planned run hours data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getPlannedRunHours() { return plannedRunHours; }
    /**
     * Performs the setPlannedRunHours operation in this module.
     *
     * @param plannedRunHours the plannedRunHours input value
     */
    public void setPlannedRunHours(BigDecimal plannedRunHours) { this.plannedRunHours = plannedRunHours; }
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
     * Retrieves planned start datetime data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getPlannedStartDatetime() { return plannedStartDatetime; }
    /**
     * Performs the setPlannedStartDatetime operation in this module.
     *
     * @param plannedStartDatetime the plannedStartDatetime input value
     */
    public void setPlannedStartDatetime(LocalDateTime plannedStartDatetime) { this.plannedStartDatetime = plannedStartDatetime; }
    /**
     * Retrieves planned end datetime data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getPlannedEndDatetime() { return plannedEndDatetime; }
    /**
     * Performs the setPlannedEndDatetime operation in this module.
     *
     * @param plannedEndDatetime the plannedEndDatetime input value
     */
    public void setPlannedEndDatetime(LocalDateTime plannedEndDatetime) { this.plannedEndDatetime = plannedEndDatetime; }
    /**
     * Retrieves actual start datetime data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getActualStartDatetime() { return actualStartDatetime; }
    /**
     * Performs the setActualStartDatetime operation in this module.
     *
     * @param actualStartDatetime the actualStartDatetime input value
     */
    public void setActualStartDatetime(LocalDateTime actualStartDatetime) { this.actualStartDatetime = actualStartDatetime; }
    /**
     * Retrieves actual end datetime data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getActualEndDatetime() { return actualEndDatetime; }
    /**
     * Performs the setActualEndDatetime operation in this module.
     *
     * @param actualEndDatetime the actualEndDatetime input value
     */
    public void setActualEndDatetime(LocalDateTime actualEndDatetime) { this.actualEndDatetime = actualEndDatetime; }
}
