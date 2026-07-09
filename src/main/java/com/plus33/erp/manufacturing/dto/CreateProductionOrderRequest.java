/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.dto
 * File              : CreateProductionOrderRequest.java
 * Purpose           : Data Transfer Object for request/response in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CreateProductionOrderController
 * Related Service   : CreateProductionOrderService, CreateProductionOrderServiceImpl
 * Related Repository: CreateProductionOrderRepository
 * Related Entity    : CreateProductionOrder
 * Related DTO       : CreateProductionOrderOperationRequest, CreateProductionOrderRequest
 * Related Mapper    : CreateProductionOrderMapper
 * Related DB Table  : create_production_orders
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CreateProductionOrderController, CreateProductionOrderService, CreateProductionOrderServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Manufacturing Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.manufacturing.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import jakarta.validation.constraints.*;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code CreateProductionOrderRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.dto}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Manufacturing Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public class CreateProductionOrderRequest {
    @NotNull private Long companyId;
    @NotBlank private String orderNumber;
    @NotNull private Long productId;
    private Long bomHeaderId;
    private Long routingHeaderId;
    @NotNull @Positive private BigDecimal plannedQuantity;
    @NotNull private Long unitId;
    private Integer priority = 5;
    @NotNull private LocalDate plannedStartDate;
    @NotNull private LocalDate plannedEndDate;
    private Long warehouseId;
    private String notes;
    private Long createdBy;
    private List<CreateProductionOrderOperationRequest> operations;

    public CreateProductionOrderRequest() {}

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
     * Retrieves unit id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getUnitId() { return unitId; }
    /**
     * Performs the setUnitId operation in this module.
     *
     * @param unitId the unitId input value
     */
    public void setUnitId(Long unitId) { this.unitId = unitId; }
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
     * Retrieves created by data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCreatedBy() { return createdBy; }
    /**
     * Performs the setCreatedBy operation in this module.
     *
     * @param createdBy the createdBy input value
     */
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    /**
     * Retrieves operations data from the database.
     *
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    public List<CreateProductionOrderOperationRequest> getOperations() { return operations; }
    /**
     * Performs the setOperations operation in this module.
     *
     * @param operations the operations input value
     */
    public void setOperations(List<CreateProductionOrderOperationRequest> operations) { this.operations = operations; }
}
