/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.dto
 * File              : EngineeringChangeOrderDto.java
 * Purpose           : Data Transfer Object for request/response in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EngineeringChangeOrderDtoController
 * Related Service   : EngineeringChangeOrderDtoService, EngineeringChangeOrderDtoServiceImpl
 * Related Repository: EngineeringChangeOrderDtoRepository
 * Related Entity    : EngineeringChangeOrderDto
 * Related DTO       : EngineeringChangeLineDto, EngineeringChangeOrderDto
 * Related Mapper    : EngineeringChangeOrderDtoMapper
 * Related DB Table  : engineering_change_order_dtos
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : EngineeringChangeOrderDtoController, EngineeringChangeOrderDtoService, EngineeringChangeOrderDtoServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Manufacturing Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.manufacturing.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code EngineeringChangeOrderDto}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.dto}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Manufacturing Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public class EngineeringChangeOrderDto {
    private Long id;
    private Long companyId;
    private String ecoNumber;
    private String title;
    private String description;
    private String reason;
    private String status;
    private String priority;
    private LocalDate effectiveDate;
    private Long requestedBy;
    private Long reviewedBy;
    private Long approvedBy;
    private LocalDateTime reviewedAt;
    private LocalDateTime approvedAt;
    private LocalDateTime implementedAt;
    private List<EngineeringChangeLineDto> lines;

    public EngineeringChangeOrderDto() {}

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
     * Retrieves eco number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getEcoNumber() { return ecoNumber; }
    /**
     * Performs the setEcoNumber operation in this module.
     *
     * @param ecoNumber the ecoNumber input value
     */
    public void setEcoNumber(String ecoNumber) { this.ecoNumber = ecoNumber; }
    /**
     * Retrieves title data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTitle() { return title; }
    /**
     * Performs the setTitle operation in this module.
     *
     * @param title the title input value
     */
    public void setTitle(String title) { this.title = title; }
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
     * Retrieves reason data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getReason() { return reason; }
    /**
     * Performs the setReason operation in this module.
     *
     * @param reason the reason input value
     */
    public void setReason(String reason) { this.reason = reason; }
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
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPriority() { return priority; }
    /**
     * Performs the setPriority operation in this module.
     *
     * @param priority the priority input value
     */
    public void setPriority(String priority) { this.priority = priority; }
    /**
     * Retrieves effective date data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getEffectiveDate() { return effectiveDate; }
    /**
     * Performs the setEffectiveDate operation in this module.
     *
     * @param effectiveDate the effectiveDate input value
     */
    public void setEffectiveDate(LocalDate effectiveDate) { this.effectiveDate = effectiveDate; }
    /**
     * Retrieves requested by data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getRequestedBy() { return requestedBy; }
    /**
     * Performs the setRequestedBy operation in this module.
     *
     * @param requestedBy the requestedBy input value
     */
    public void setRequestedBy(Long requestedBy) { this.requestedBy = requestedBy; }
    /**
     * Retrieves reviewed by data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getReviewedBy() { return reviewedBy; }
    /**
     * Performs the setReviewedBy operation in this module.
     *
     * @param reviewedBy the reviewedBy input value
     */
    public void setReviewedBy(Long reviewedBy) { this.reviewedBy = reviewedBy; }
    /**
     * Retrieves approved by data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getApprovedBy() { return approvedBy; }
    /**
     * Performs the setApprovedBy operation in this module.
     *
     * @param approvedBy the approvedBy input value
     */
    public void setApprovedBy(Long approvedBy) { this.approvedBy = approvedBy; }
    /**
     * Retrieves reviewed at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getReviewedAt() { return reviewedAt; }
    /**
     * Performs the setReviewedAt operation in this module.
     *
     * @param reviewedAt the reviewedAt input value
     */
    public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }
    /**
     * Retrieves approved at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getApprovedAt() { return approvedAt; }
    /**
     * Performs the setApprovedAt operation in this module.
     *
     * @param approvedAt the approvedAt input value
     */
    public void setApprovedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; }
    /**
     * Retrieves implemented at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getImplementedAt() { return implementedAt; }
    /**
     * Performs the setImplementedAt operation in this module.
     *
     * @param implementedAt the implementedAt input value
     */
    public void setImplementedAt(LocalDateTime implementedAt) { this.implementedAt = implementedAt; }
    /**
     * Retrieves lines data from the database.
     *
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    public List<EngineeringChangeLineDto> getLines() { return lines; }
    /**
     * Performs the setLines operation in this module.
     *
     * @param lines the lines input value
     */
    public void setLines(List<EngineeringChangeLineDto> lines) { this.lines = lines; }
}
