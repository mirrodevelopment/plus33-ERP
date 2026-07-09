/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.dto
 * File              : ProductionCostDto.java
 * Purpose           : Data Transfer Object for request/response in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProductionCostDtoController
 * Related Service   : ProductionCostDtoService, ProductionCostDtoServiceImpl
 * Related Repository: ProductionCostDtoRepository
 * Related Entity    : ProductionCostDto
 * Related DTO       : ProductionCostDto
 * Related Mapper    : ProductionCostDtoMapper
 * Related DB Table  : production_cost_dtos
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProductionCostDtoController, ProductionCostDtoService, ProductionCostDtoServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Manufacturing Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.manufacturing.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductionCostDto {
    private Long id;
    private Long productionOrderId;
    private String costCategory;
    private String description;
    private BigDecimal plannedAmount;
    private BigDecimal actualAmount;
    private BigDecimal varianceAmount;
    private Long referenceEntityId;
    private String referenceEntityType;
    private LocalDateTime recordedAt;

    private String costingMethod;
    private BigDecimal actualMaterialCost;
    private BigDecimal actualLaborCost;
    private BigDecimal actualMachineCost;
    private BigDecimal actualOverheadCost;
    private BigDecimal actualSubcontractCost;
    private BigDecimal actualTotalCost;
    private BigDecimal standardMaterialCost;
    private BigDecimal standardLaborCost;
    private BigDecimal standardMachineCost;
    private BigDecimal standardOverheadCost;
    private BigDecimal standardSubcontractCost;
    private BigDecimal standardTotalCost;
    private BigDecimal wipBalance;
    private String status;

    public ProductionCostDto() {}

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
     * Retrieves cost category data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCostCategory() { return costCategory; }
    /**
     * Performs the setCostCategory operation in this module.
     *
     * @param costCategory the costCategory input value
     */
    public void setCostCategory(String costCategory) { this.costCategory = costCategory; }
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
     * Retrieves planned amount data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getPlannedAmount() { return plannedAmount; }
    /**
     * Performs the setPlannedAmount operation in this module.
     *
     * @param plannedAmount the plannedAmount input value
     */
    public void setPlannedAmount(BigDecimal plannedAmount) { this.plannedAmount = plannedAmount; }
    /**
     * Retrieves actual amount data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getActualAmount() { return actualAmount; }
    /**
     * Performs the setActualAmount operation in this module.
     *
     * @param actualAmount the actualAmount input value
     */
    public void setActualAmount(BigDecimal actualAmount) { this.actualAmount = actualAmount; }
    /**
     * Retrieves variance amount data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getVarianceAmount() { return varianceAmount; }
    /**
     * Performs the setVarianceAmount operation in this module.
     *
     * @param varianceAmount the varianceAmount input value
     */
    public void setVarianceAmount(BigDecimal varianceAmount) { this.varianceAmount = varianceAmount; }
    /**
     * Retrieves reference entity id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getReferenceEntityId() { return referenceEntityId; }
    /**
     * Performs the setReferenceEntityId operation in this module.
     *
     * @param referenceEntityId the referenceEntityId input value
     */
    public void setReferenceEntityId(Long referenceEntityId) { this.referenceEntityId = referenceEntityId; }
    /**
     * Retrieves reference entity type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getReferenceEntityType() { return referenceEntityType; }
    /**
     * Performs the setReferenceEntityType operation in this module.
     *
     * @param referenceEntityType the referenceEntityType input value
     */
    public void setReferenceEntityType(String referenceEntityType) { this.referenceEntityType = referenceEntityType; }
    /**
     * Retrieves recorded at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getRecordedAt() { return recordedAt; }
    /**
     * Performs the setRecordedAt operation in this module.
     *
     * @param recordedAt the recordedAt input value
     */
    public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }

    /**
     * Retrieves costing method data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCostingMethod() { return costingMethod; }
    /**
     * Performs the setCostingMethod operation in this module.
     *
     * @param costingMethod the costingMethod input value
     */
    public void setCostingMethod(String costingMethod) { this.costingMethod = costingMethod; }
    /**
     * Retrieves actual material cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getActualMaterialCost() { return actualMaterialCost; }
    /**
     * Performs the setActualMaterialCost operation in this module.
     *
     * @param actualMaterialCost the actualMaterialCost input value
     */
    public void setActualMaterialCost(BigDecimal actualMaterialCost) { this.actualMaterialCost = actualMaterialCost; }
    /**
     * Retrieves a paginated list of actual labor cost records.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getActualLaborCost() { return actualLaborCost; }
    /**
     * Performs the setActualLaborCost operation in this module.
     *
     * @param actualLaborCost the actualLaborCost input value
     */
    public void setActualLaborCost(BigDecimal actualLaborCost) { this.actualLaborCost = actualLaborCost; }
    /**
     * Retrieves actual machine cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getActualMachineCost() { return actualMachineCost; }
    /**
     * Performs the setActualMachineCost operation in this module.
     *
     * @param actualMachineCost the actualMachineCost input value
     */
    public void setActualMachineCost(BigDecimal actualMachineCost) { this.actualMachineCost = actualMachineCost; }
    /**
     * Retrieves actual overhead cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getActualOverheadCost() { return actualOverheadCost; }
    /**
     * Performs the setActualOverheadCost operation in this module.
     *
     * @param actualOverheadCost the actualOverheadCost input value
     */
    public void setActualOverheadCost(BigDecimal actualOverheadCost) { this.actualOverheadCost = actualOverheadCost; }
    /**
     * Retrieves actual subcontract cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getActualSubcontractCost() { return actualSubcontractCost; }
    /**
     * Performs the setActualSubcontractCost operation in this module.
     *
     * @param actualSubcontractCost the actualSubcontractCost input value
     */
    public void setActualSubcontractCost(BigDecimal actualSubcontractCost) { this.actualSubcontractCost = actualSubcontractCost; }
    /**
     * Retrieves actual total cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getActualTotalCost() { return actualTotalCost; }
    /**
     * Performs the setActualTotalCost operation in this module.
     *
     * @param actualTotalCost the actualTotalCost input value
     */
    public void setActualTotalCost(BigDecimal actualTotalCost) { this.actualTotalCost = actualTotalCost; }
    /**
     * Retrieves standard material cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getStandardMaterialCost() { return standardMaterialCost; }
    /**
     * Performs the setStandardMaterialCost operation in this module.
     *
     * @param standardMaterialCost the standardMaterialCost input value
     */
    public void setStandardMaterialCost(BigDecimal standardMaterialCost) { this.standardMaterialCost = standardMaterialCost; }
    /**
     * Retrieves standard labor cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getStandardLaborCost() { return standardLaborCost; }
    /**
     * Performs the setStandardLaborCost operation in this module.
     *
     * @param standardLaborCost the standardLaborCost input value
     */
    public void setStandardLaborCost(BigDecimal standardLaborCost) { this.standardLaborCost = standardLaborCost; }
    /**
     * Retrieves standard machine cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getStandardMachineCost() { return standardMachineCost; }
    /**
     * Performs the setStandardMachineCost operation in this module.
     *
     * @param standardMachineCost the standardMachineCost input value
     */
    public void setStandardMachineCost(BigDecimal standardMachineCost) { this.standardMachineCost = standardMachineCost; }
    /**
     * Retrieves standard overhead cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getStandardOverheadCost() { return standardOverheadCost; }
    /**
     * Performs the setStandardOverheadCost operation in this module.
     *
     * @param standardOverheadCost the standardOverheadCost input value
     */
    public void setStandardOverheadCost(BigDecimal standardOverheadCost) { this.standardOverheadCost = standardOverheadCost; }
    /**
     * Retrieves standard subcontract cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getStandardSubcontractCost() { return standardSubcontractCost; }
    /**
     * Performs the setStandardSubcontractCost operation in this module.
     *
     * @param standardSubcontractCost the standardSubcontractCost input value
     */
    public void setStandardSubcontractCost(BigDecimal standardSubcontractCost) { this.standardSubcontractCost = standardSubcontractCost; }
    /**
     * Retrieves standard total cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getStandardTotalCost() { return standardTotalCost; }
    /**
     * Performs the setStandardTotalCost operation in this module.
     *
     * @param standardTotalCost the standardTotalCost input value
     */
    public void setStandardTotalCost(BigDecimal standardTotalCost) { this.standardTotalCost = standardTotalCost; }
    /**
     * Retrieves wip balance data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getWipBalance() { return wipBalance; }
    /**
     * Performs the setWipBalance operation in this module.
     *
     * @param wipBalance the wipBalance input value
     */
    public void setWipBalance(BigDecimal wipBalance) { this.wipBalance = wipBalance; }
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
}
