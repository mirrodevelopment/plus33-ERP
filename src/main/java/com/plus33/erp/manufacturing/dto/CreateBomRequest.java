/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.dto
 * File              : CreateBomRequest.java
 * Purpose           : Data Transfer Object for request/response in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CreateBomController
 * Related Service   : CreateBomService, CreateBomServiceImpl
 * Related Repository: CreateBomRepository
 * Related Entity    : CreateBom
 * Related DTO       : CreateBomRequest
 * Related Mapper    : CreateBomMapper
 * Related DB Table  : create_boms
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CreateBomController, CreateBomService, CreateBomServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Manufacturing Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.manufacturing.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.constraints.*;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code CreateBomRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.dto}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Manufacturing Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public class CreateBomRequest {
    @NotNull private Long companyId;
    @NotNull private Long productId;
    @NotBlank private String bomNumber;
    @NotNull @Positive private BigDecimal baseQuantity;
    @NotNull private Long unitId;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private Long routingHeaderId;
    private String notes;
    private Long createdBy;

    public CreateBomRequest() {}

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
     * Retrieves bom number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getBomNumber() { return bomNumber; }
    /**
     * Performs the setBomNumber operation in this module.
     *
     * @param bomNumber the bomNumber input value
     */
    public void setBomNumber(String bomNumber) { this.bomNumber = bomNumber; }
    /**
     * Retrieves bom code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getBomCode() { return bomNumber; }
    /**
     * Performs the setBomCode operation in this module.
     *
     * @param bomCode the bomCode input value
     */
    public void setBomCode(String bomCode) { this.bomNumber = bomCode; }
    /**
     * Retrieves base quantity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getBaseQuantity() { return baseQuantity; }
    /**
     * Performs the setBaseQuantity operation in this module.
     *
     * @param baseQuantity the baseQuantity input value
     */
    public void setBaseQuantity(BigDecimal baseQuantity) { this.baseQuantity = baseQuantity; }
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
     * Retrieves effective from data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getEffectiveFrom() { return effectiveFrom; }
    /**
     * Performs the setEffectiveFrom operation in this module.
     *
     * @param effectiveFrom the effectiveFrom input value
     */
    public void setEffectiveFrom(LocalDate effectiveFrom) { this.effectiveFrom = effectiveFrom; }
    /**
     * Retrieves effective to data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getEffectiveTo() { return effectiveTo; }
    /**
     * Performs the setEffectiveTo operation in this module.
     *
     * @param effectiveTo the effectiveTo input value
     */
    public void setEffectiveTo(LocalDate effectiveTo) { this.effectiveTo = effectiveTo; }
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
}
