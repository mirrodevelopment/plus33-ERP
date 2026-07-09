/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.dto
 * File              : BomLineDto.java
 * Purpose           : Data Transfer Object for request/response in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BomLineDtoController
 * Related Service   : BomLineDtoService, BomLineDtoServiceImpl
 * Related Repository: BomLineDtoRepository
 * Related Entity    : BomLineDto
 * Related DTO       : BomLineDto
 * Related Mapper    : BomLineDtoMapper
 * Related DB Table  : bom_line_dtos
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BomLineDtoController, BomLineDtoService, BomLineDtoServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Manufacturing Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.manufacturing.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BomLineDto {
    private Long id;
    private Long bomHeaderId;
    private Integer lineNumber;
    private Integer sortSequence;
    private Long componentProductId;
    private String componentProductCode;
    private String componentProductName;
    private BigDecimal quantity;
    private String unitCode;
    private String componentType;
    private Boolean phantom;
    private BigDecimal scrapPercentage;
    private BigDecimal yieldPercentage;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private Long substituteProductId;
    private String notes;
    private Long unitId;

    public BomLineDto() {}

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
     * Retrieves component product code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getComponentProductCode() { return componentProductCode; }
    /**
     * Performs the setComponentProductCode operation in this module.
     *
     * @param componentProductCode the componentProductCode input value
     */
    public void setComponentProductCode(String componentProductCode) { this.componentProductCode = componentProductCode; }
    /**
     * Retrieves component product name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getComponentProductName() { return componentProductName; }
    /**
     * Performs the setComponentProductName operation in this module.
     *
     * @param componentProductName the componentProductName input value
     */
    public void setComponentProductName(String componentProductName) { this.componentProductName = componentProductName; }
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
