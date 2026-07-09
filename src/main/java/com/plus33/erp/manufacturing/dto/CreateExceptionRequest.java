/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.dto
 * File              : CreateExceptionRequest.java
 * Purpose           : Data Transfer Object for request/response in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CreateExceptionController
 * Related Service   : CreateExceptionService, CreateExceptionServiceImpl
 * Related Repository: CreateExceptionRepository
 * Related Entity    : CreateException
 * Related DTO       : CreateExceptionRequest
 * Related Mapper    : CreateExceptionMapper
 * Related DB Table  : create_exceptions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CreateExceptionController, CreateExceptionService, CreateExceptionServiceImpl
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
 * <p><b>Class  :</b> {@code CreateExceptionRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.dto}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Manufacturing Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public class CreateExceptionRequest {
    @NotNull private LocalDate exceptionDate;
    @NotBlank private String exceptionType; // HOLIDAY, MAINTENANCE_WINDOW, OVERTIME, SHUTDOWN
    @NotNull private BigDecimal availableHours;
    private String description;

    public CreateExceptionRequest() {}

    /**
     * Retrieves exception date data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getExceptionDate() { return exceptionDate; }
    /**
     * Performs the setExceptionDate operation in this module.
     *
     * @param exceptionDate the exceptionDate input value
     */
    public void setExceptionDate(LocalDate exceptionDate) { this.exceptionDate = exceptionDate; }
    /**
     * Retrieves exception type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getExceptionType() { return exceptionType; }
    /**
     * Performs the setExceptionType operation in this module.
     *
     * @param exceptionType the exceptionType input value
     */
    public void setExceptionType(String exceptionType) { this.exceptionType = exceptionType; }
    /**
     * Retrieves available hours data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getAvailableHours() { return availableHours; }
    /**
     * Performs the setAvailableHours operation in this module.
     *
     * @param availableHours the availableHours input value
     */
    public void setAvailableHours(BigDecimal availableHours) { this.availableHours = availableHours; }
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
}
