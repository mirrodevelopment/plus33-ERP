/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.dto
 * File              : CreateEcoRequest.java
 * Purpose           : Data Transfer Object for request/response in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CreateEcoController
 * Related Service   : CreateEcoService, CreateEcoServiceImpl
 * Related Repository: CreateEcoRepository
 * Related Entity    : CreateEco
 * Related DTO       : CreateEcoLineRequest, CreateEcoRequest
 * Related Mapper    : CreateEcoMapper
 * Related DB Table  : create_ecos
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CreateEcoController, CreateEcoService, CreateEcoServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Manufacturing Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.manufacturing.dto;

import java.time.LocalDate;
import java.util.List;
import jakarta.validation.constraints.*;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code CreateEcoRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.dto}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Manufacturing Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public class CreateEcoRequest {
    @NotNull private Long companyId;
    @NotBlank private String ecoNumber;
    @NotBlank private String title;
    private String description;
    private String reason;
    private String priority = "NORMAL"; // LOW, NORMAL, HIGH, CRITICAL, SAFETY
    private LocalDate effectiveDate;
    @NotNull private Long requestedBy;
    private List<CreateEcoLineRequest> lines;

    public CreateEcoRequest() {}

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
     * Retrieves lines data from the database.
     *
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    public List<CreateEcoLineRequest> getLines() { return lines; }
    /**
     * Performs the setLines operation in this module.
     *
     * @param lines the lines input value
     */
    public void setLines(List<CreateEcoLineRequest> lines) { this.lines = lines; }
}
