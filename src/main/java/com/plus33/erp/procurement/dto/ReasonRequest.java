/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.dto
 * File              : ReasonRequest.java
 * Purpose           : Data Transfer Object for request/response in Procurement Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ReasonController
 * Related Service   : ReasonService, ReasonServiceImpl
 * Related Repository: ReasonRepository
 * Related Entity    : Reason
 * Related DTO       : ReasonRequest
 * Related Mapper    : ReasonMapper
 * Related DB Table  : reasons
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ReasonController, ReasonService, ReasonServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Procurement Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.procurement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code ReasonRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Procurement Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Schema(description = "Request body containing reason details for rejection or cancellation")
public record ReasonRequest(
        @Schema(description = "Reason description", example = "Price too high / duplicate request")
        @NotBlank(message = "Reason is required")
        String reason
) {}