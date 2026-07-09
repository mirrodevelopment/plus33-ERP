/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.dto
 * File              : ProductionCostSummaryDto.java
 * Purpose           : Data Transfer Object for request/response in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProductionCostSummaryDtoController
 * Related Service   : ProductionCostSummaryDtoService, ProductionCostSummaryDtoServiceImpl
 * Related Repository: ProductionCostSummaryDtoRepository
 * Related Entity    : ProductionCostSummaryDto
 * Related DTO       : ProductionCostDto, ProductionCostSummaryDto
 * Related Mapper    : ProductionCostSummaryDtoMapper
 * Related DB Table  : production_cost_summary_dtos
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProductionCostSummaryDtoController, ProductionCostSummaryDtoService, ProductionCostSummaryDtoServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Manufacturing Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.manufacturing.dto;

import java.math.BigDecimal;
import java.util.List;

public class ProductionCostSummaryDto {
    private Long productionOrderId;
    private String orderNumber;
    private BigDecimal totalPlannedCost;
    private BigDecimal totalActualCost;
    private BigDecimal totalVariance;
    private BigDecimal materialCost;
    private BigDecimal laborCost;
    private BigDecimal overheadCost;
    private List<ProductionCostDto> lineItems;

    private BigDecimal materialVariance;
    private BigDecimal laborVariance;
    private BigDecimal machineVariance;
    private BigDecimal overheadVariance;
    private BigDecimal wipBalance;

    public ProductionCostSummaryDto() {}

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
     * Retrieves total planned cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getTotalPlannedCost() { return totalPlannedCost; }
    /**
     * Performs the setTotalPlannedCost operation in this module.
     *
     * @param totalPlannedCost the totalPlannedCost input value
     */
    public void setTotalPlannedCost(BigDecimal totalPlannedCost) { this.totalPlannedCost = totalPlannedCost; }
    /**
     * Retrieves total actual cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getTotalActualCost() { return totalActualCost; }
    /**
     * Performs the setTotalActualCost operation in this module.
     *
     * @param totalActualCost the totalActualCost input value
     */
    public void setTotalActualCost(BigDecimal totalActualCost) { this.totalActualCost = totalActualCost; }
    /**
     * Retrieves total variance data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getTotalVariance() { return totalVariance; }
    /**
     * Performs the setTotalVariance operation in this module.
     *
     * @param totalVariance the totalVariance input value
     */
    public void setTotalVariance(BigDecimal totalVariance) { this.totalVariance = totalVariance; }
    /**
     * Retrieves material cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getMaterialCost() { return materialCost; }
    /**
     * Performs the setMaterialCost operation in this module.
     *
     * @param materialCost the materialCost input value
     */
    public void setMaterialCost(BigDecimal materialCost) { this.materialCost = materialCost; }
    /**
     * Retrieves labor cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getLaborCost() { return laborCost; }
    /**
     * Performs the setLaborCost operation in this module.
     *
     * @param laborCost the laborCost input value
     */
    public void setLaborCost(BigDecimal laborCost) { this.laborCost = laborCost; }
    /**
     * Retrieves overhead cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getOverheadCost() { return overheadCost; }
    /**
     * Performs the setOverheadCost operation in this module.
     *
     * @param overheadCost the overheadCost input value
     */
    public void setOverheadCost(BigDecimal overheadCost) { this.overheadCost = overheadCost; }
    /**
     * Retrieves line items data from the database.
     *
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    public List<ProductionCostDto> getLineItems() { return lineItems; }
    /**
     * Performs the setLineItems operation in this module.
     *
     * @param lineItems the lineItems input value
     */
    public void setLineItems(List<ProductionCostDto> lineItems) { this.lineItems = lineItems; }

    /**
     * Retrieves material variance data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getMaterialVariance() { return materialVariance; }
    /**
     * Performs the setMaterialVariance operation in this module.
     *
     * @param materialVariance the materialVariance input value
     */
    public void setMaterialVariance(BigDecimal materialVariance) { this.materialVariance = materialVariance; }
    /**
     * Retrieves labor variance data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getLaborVariance() { return laborVariance; }
    /**
     * Performs the setLaborVariance operation in this module.
     *
     * @param laborVariance the laborVariance input value
     */
    public void setLaborVariance(BigDecimal laborVariance) { this.laborVariance = laborVariance; }
    /**
     * Retrieves machine variance data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getMachineVariance() { return machineVariance; }
    /**
     * Performs the setMachineVariance operation in this module.
     *
     * @param machineVariance the machineVariance input value
     */
    public void setMachineVariance(BigDecimal machineVariance) { this.machineVariance = machineVariance; }
    /**
     * Retrieves overhead variance data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getOverheadVariance() { return overheadVariance; }
    /**
     * Performs the setOverheadVariance operation in this module.
     *
     * @param overheadVariance the overheadVariance input value
     */
    public void setOverheadVariance(BigDecimal overheadVariance) { this.overheadVariance = overheadVariance; }
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
}
