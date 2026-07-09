/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.dto
 * File              : CreateBomLineRequest.java
 * Purpose           : Data Transfer Object for request/response in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CreateBomLineController
 * Related Service   : CreateBomLineService, CreateBomLineServiceImpl
 * Related Repository: CreateBomLineRepository
 * Related Entity    : CreateBomLine
 * Related DTO       : CreateBomLineRequest
 * Related Mapper    : CreateBomLineMapper
 * Related DB Table  : create_bom_lines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CreateBomLineController, CreateBomLineService, CreateBomLineServiceImpl
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
 * <p><b>Class  :</b> {@code CreateBomLineRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.dto}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Manufacturing Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public class CreateBomLineRequest {
    @NotNull private Long componentProductId;
    @NotNull @Positive private BigDecimal quantity;
    @NotNull private Long unitId;
    private Integer lineNumber;
    private Integer sortSequence;
    private String componentType = "COMPONENT";
    private Boolean phantom = false;
    private BigDecimal scrapPercentage = BigDecimal.ZERO;
    private BigDecimal yieldPercentage = new BigDecimal("100.00");
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private Long substituteProductId;
    private String notes;

    public CreateBomLineRequest() {}

    /**
     * Retrieves line number data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getLineNumber() { return lineNumber; }
    /**
     * Performs the setLineNumber operation in this module.
     *
     * @param lineNumber the lineNumber input value
     */
    public void setLineNumber(Integer lineNumber) { this.lineNumber = lineNumber; }

    /**
     * Retrieves component product id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getComponentProductId() { return componentProductId; }
    /**
     * Performs the setComponentProductId operation in this module.
     *
     * @param componentProductId the componentProductId input value
     */
    public void setComponentProductId(Long componentProductId) { this.componentProductId = componentProductId; }
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
     * Retrieves sort sequence data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getSortSequence() { return sortSequence; }
    /**
     * Performs the setSortSequence operation in this module.
     *
     * @param sortSequence the sortSequence input value
     */
    public void setSortSequence(Integer sortSequence) { this.sortSequence = sortSequence; }
    /**
     * Retrieves component type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getComponentType() { return componentType; }
    /**
     * Performs the setComponentType operation in this module.
     *
     * @param componentType the componentType input value
     */
    public void setComponentType(String componentType) { this.componentType = componentType; }
    /**
     * Retrieves phantom data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getPhantom() { return phantom; }
    /**
     * Performs the setPhantom operation in this module.
     *
     * @param phantom the phantom input value
     */
    public void setPhantom(Boolean phantom) { this.phantom = phantom; }
    /**
     * Retrieves scrap percentage data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getScrapPercentage() { return scrapPercentage; }
    /**
     * Performs the setScrapPercentage operation in this module.
     *
     * @param scrapPercentage the scrapPercentage input value
     */
    public void setScrapPercentage(BigDecimal scrapPercentage) { this.scrapPercentage = scrapPercentage; }
    /**
     * Retrieves yield percentage data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getYieldPercentage() { return yieldPercentage; }
    /**
     * Performs the setYieldPercentage operation in this module.
     *
     * @param yieldPercentage the yieldPercentage input value
     */
    public void setYieldPercentage(BigDecimal yieldPercentage) { this.yieldPercentage = yieldPercentage; }
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
     * Retrieves substitute product id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getSubstituteProductId() { return substituteProductId; }
    /**
     * Performs the setSubstituteProductId operation in this module.
     *
     * @param substituteProductId the substituteProductId input value
     */
    public void setSubstituteProductId(Long substituteProductId) { this.substituteProductId = substituteProductId; }
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
}
