/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.dto
 * File              : EngineeringChangeLineDto.java
 * Purpose           : Data Transfer Object for request/response in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EngineeringChangeLineDtoController
 * Related Service   : EngineeringChangeLineDtoService, EngineeringChangeLineDtoServiceImpl
 * Related Repository: EngineeringChangeLineDtoRepository
 * Related Entity    : EngineeringChangeLineDto
 * Related DTO       : EngineeringChangeLineDto
 * Related Mapper    : EngineeringChangeLineDtoMapper
 * Related DB Table  : engineering_change_line_dtos
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : EngineeringChangeLineDtoController, EngineeringChangeLineDtoService, EngineeringChangeLineDtoServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Manufacturing Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.manufacturing.dto;

import java.time.LocalDate;

public class EngineeringChangeLineDto {
    private Long id;
    private Long ecoId;
    private String changeType; // BOM_ADD, BOM_REMOVE, BOM_MODIFY, ROUTING_ADD, ROUTING_REMOVE, ROUTING_MODIFY
    private String referenceType; // BOM_HEADER, BOM_LINE, ROUTING_HEADER, ROUTING_OPERATION
    private Long referenceId;
    private String beforeSnapshot;
    private String afterSnapshot;
    private LocalDate effectiveFrom;
    private Integer sortSequence;
    private String notes;

    public EngineeringChangeLineDto() {}

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
     * Retrieves eco id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getEcoId() { return ecoId; }
    /**
     * Performs the setEcoId operation in this module.
     *
     * @param ecoId the ecoId input value
     */
    public void setEcoId(Long ecoId) { this.ecoId = ecoId; }
    /**
     * Retrieves change type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getChangeType() { return changeType; }
    /**
     * Performs the setChangeType operation in this module.
     *
     * @param changeType the changeType input value
     */
    public void setChangeType(String changeType) { this.changeType = changeType; }
    /**
     * Retrieves reference type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getReferenceType() { return referenceType; }
    /**
     * Performs the setReferenceType operation in this module.
     *
     * @param referenceType the referenceType input value
     */
    public void setReferenceType(String referenceType) { this.referenceType = referenceType; }
    /**
     * Retrieves reference id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getReferenceId() { return referenceId; }
    /**
     * Performs the setReferenceId operation in this module.
     *
     * @param referenceId the referenceId input value
     */
    public void setReferenceId(Long referenceId) { this.referenceId = referenceId; }
    /**
     * Retrieves before snapshot data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getBeforeSnapshot() { return beforeSnapshot; }
    /**
     * Performs the setBeforeSnapshot operation in this module.
     *
     * @param beforeSnapshot the beforeSnapshot input value
     */
    public void setBeforeSnapshot(String beforeSnapshot) { this.beforeSnapshot = beforeSnapshot; }
    /**
     * Retrieves after snapshot data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getAfterSnapshot() { return afterSnapshot; }
    /**
     * Performs the setAfterSnapshot operation in this module.
     *
     * @param afterSnapshot the afterSnapshot input value
     */
    public void setAfterSnapshot(String afterSnapshot) { this.afterSnapshot = afterSnapshot; }
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
