/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.dto
 * File              : ProductionOrderDto.java
 * Purpose           : Data Transfer Object for request/response in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProductionOrderDtoController
 * Related Service   : ProductionOrderDtoService, ProductionOrderDtoServiceImpl
 * Related Repository: ProductionOrderDtoRepository
 * Related Entity    : ProductionOrderDto
 * Related DTO       : ProductionOrderDto, ProductionOrderOperationDto
 * Related Mapper    : ProductionOrderDtoMapper
 * Related DB Table  : production_order_dtos
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProductionOrderDtoController, ProductionOrderDtoService, ProductionOrderDtoServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Manufacturing Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.manufacturing.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code ProductionOrderDto}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.dto}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Manufacturing Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public class ProductionOrderDto {
    private Long id;
    private Long companyId;
    private String orderNumber;
    private Long productId;
    private String productCode;
    private String productName;
    private Long bomHeaderId;
    private Long routingHeaderId;
    private BigDecimal plannedQuantity;
    private BigDecimal completedQuantity;
    private BigDecimal scrappedQuantity;
    private String unitCode;
    private String status;
    private Integer priority;
    private LocalDate plannedStartDate;
    private LocalDate plannedEndDate;
    private LocalDate actualStartDate;
    private LocalDate actualEndDate;
    private Long warehouseId;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ProductionOrderOperationDto> operations;

    public ProductionOrderDto() {}

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
     * Retrieves order number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getOrderNumber() { return orderNumber; }
    /**
     * Performs the setOrderNumber operation in this module.
     *
     * @param orderNumber the orderNumber input value
     */
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
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
     * Retrieves product code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getProductCode() { return productCode; }
    /**
     * Performs the setProductCode operation in this module.
     *
     * @param productCode the productCode input value
     */
    public void setProductCode(String productCode) { this.productCode = productCode; }
    /**
     * Retrieves product name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getProductName() { return productName; }
    /**
     * Performs the setProductName operation in this module.
     *
     * @param productName the productName input value
     */
    public void setProductName(String productName) { this.productName = productName; }
    /**
     * Retrieves bom header id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getBomHeaderId() { return bomHeaderId; }
    /**
     * Performs the setBomHeaderId operation in this module.
     *
     * @param bomHeaderId the bomHeaderId input value
     */
    public void setBomHeaderId(Long bomHeaderId) { this.bomHeaderId = bomHeaderId; }
    /**
     * Retrieves routing header id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getRoutingHeaderId() { return routingHeaderId; }
    /**
     * Performs the setRoutingHeaderId operation in this module.
     *
     * @param routingHeaderId the routingHeaderId input value
     */
    public void setRoutingHeaderId(Long routingHeaderId) { this.routingHeaderId = routingHeaderId; }
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
     * Retrieves unit code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getUnitCode() { return unitCode; }
    /**
     * Performs the setUnitCode operation in this module.
     *
     * @param unitCode the unitCode input value
     */
    public void setUnitCode(String unitCode) { this.unitCode = unitCode; }
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
     * Retrieves priority data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getPriority() { return priority; }
    /**
     * Performs the setPriority operation in this module.
     *
     * @param priority the priority input value
     */
    public void setPriority(Integer priority) { this.priority = priority; }
    /**
     * Retrieves planned start date data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getPlannedStartDate() { return plannedStartDate; }
    /**
     * Performs the setPlannedStartDate operation in this module.
     *
     * @param plannedStartDate the plannedStartDate input value
     */
    public void setPlannedStartDate(LocalDate plannedStartDate) { this.plannedStartDate = plannedStartDate; }
    /**
     * Retrieves planned end date data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getPlannedEndDate() { return plannedEndDate; }
    /**
     * Performs the setPlannedEndDate operation in this module.
     *
     * @param plannedEndDate the plannedEndDate input value
     */
    public void setPlannedEndDate(LocalDate plannedEndDate) { this.plannedEndDate = plannedEndDate; }
    /**
     * Retrieves actual start date data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getActualStartDate() { return actualStartDate; }
    /**
     * Performs the setActualStartDate operation in this module.
     *
     * @param actualStartDate the actualStartDate input value
     */
    public void setActualStartDate(LocalDate actualStartDate) { this.actualStartDate = actualStartDate; }
    /**
     * Retrieves actual end date data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getActualEndDate() { return actualEndDate; }
    /**
     * Performs the setActualEndDate operation in this module.
     *
     * @param actualEndDate the actualEndDate input value
     */
    public void setActualEndDate(LocalDate actualEndDate) { this.actualEndDate = actualEndDate; }
    /**
     * Retrieves warehouse id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getWarehouseId() { return warehouseId; }
    /**
     * Performs the setWarehouseId operation in this module.
     *
     * @param warehouseId the warehouseId input value
     */
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }
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
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
    /**
     * Performs the setCreatedAt operation in this module.
     *
     * @param createdAt the createdAt input value
     */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    /**
     * Retrieves updated at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    /**
     * Performs the setUpdatedAt operation in this module.
     *
     * @param updatedAt the updatedAt input value
     */
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    /**
     * Retrieves operations data from the database.
     *
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    public List<ProductionOrderOperationDto> getOperations() { return operations; }
    /**
     * Performs the setOperations operation in this module.
     *
     * @param operations the operations input value
     */
    public void setOperations(List<ProductionOrderOperationDto> operations) { this.operations = operations; }
}
