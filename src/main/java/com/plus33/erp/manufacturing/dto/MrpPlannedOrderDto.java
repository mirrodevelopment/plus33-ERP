/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.dto
 * File              : MrpPlannedOrderDto.java
 * Purpose           : Data Transfer Object for request/response in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: MrpPlannedOrderDtoController
 * Related Service   : MrpPlannedOrderDtoService, MrpPlannedOrderDtoServiceImpl
 * Related Repository: MrpPlannedOrderDtoRepository
 * Related Entity    : MrpPlannedOrderDto
 * Related DTO       : MrpPlannedOrderDto
 * Related Mapper    : MrpPlannedOrderDtoMapper
 * Related DB Table  : mrp_planned_order_dtos
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : MrpPlannedOrderDtoController, MrpPlannedOrderDtoService, MrpPlannedOrderDtoServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Manufacturing Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.manufacturing.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MrpPlannedOrderDto {
    private Long id;
    private Long mrpRunId;
    private Long companyId;
    private Long productId;
    private String productCode;
    private String productName;
    private String orderType;
    private BigDecimal quantity;
    private String unitCode;
    private LocalDate releaseDate;
    private LocalDate dueDate;
    private String status;
    private Boolean firmed;
    private Long releasedProductionOrderId;

    public MrpPlannedOrderDto() {}

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
     * Retrieves mrp run id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getMrpRunId() { return mrpRunId; }
    /**
     * Performs the setMrpRunId operation in this module.
     *
     * @param mrpRunId the mrpRunId input value
     */
    public void setMrpRunId(Long mrpRunId) { this.mrpRunId = mrpRunId; }
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
     * Retrieves order type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getOrderType() { return orderType; }
    /**
     * Performs the setOrderType operation in this module.
     *
     * @param orderType the orderType input value
     */
    public void setOrderType(String orderType) { this.orderType = orderType; }
    /**
     * Retrieves quantity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getQuantity() { return quantity; }
    /**
     * Performs the setQuantity operation in this module.
     *
     * @param quantity the quantity input value
     */
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
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
     * Retrieves release date data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getReleaseDate() { return releaseDate; }
    /**
     * Performs the setReleaseDate operation in this module.
     *
     * @param releaseDate the releaseDate input value
     */
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }
    /**
     * Retrieves due date data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getDueDate() { return dueDate; }
    /**
     * Performs the setDueDate operation in this module.
     *
     * @param dueDate the dueDate input value
     */
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
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
     * Retrieves firmed data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getFirmed() { return firmed; }
    /**
     * Performs the setFirmed operation in this module.
     *
     * @param firmed the firmed input value
     */
    public void setFirmed(Boolean firmed) { this.firmed = firmed; }
    /**
     * Retrieves released production order id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getReleasedProductionOrderId() { return releasedProductionOrderId; }
    /**
     * Performs the setReleasedProductionOrderId operation in this module.
     *
     * @param releasedProductionOrderId the releasedProductionOrderId input value
     */
    public void setReleasedProductionOrderId(Long releasedProductionOrderId) { this.releasedProductionOrderId = releasedProductionOrderId; }
}
